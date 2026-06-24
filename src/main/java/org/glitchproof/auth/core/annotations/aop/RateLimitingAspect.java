package org.glitchproof.auth.core.annotations.aop;

import java.util.Map;
import java.time.Duration;
import io.github.bucket4j.Bucket;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import io.github.bucket4j.Bandwidth;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import io.github.bucket4j.BucketConfiguration;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.glitchproof.auth.core.annotations.RateLimiter;
import org.glitchproof.auth.config.security.UniqueIdFilter;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.web.context.request.RequestContextHolder;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.glitchproof.auth.core.annotations.exceptions.RateLimiterException;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitingAspect {
    private final LettuceBasedProxyManager<String> lettuceBasedProxyManager;
    private final Map<RateLimiter, BucketConfiguration> bucketConfigCache = new ConcurrentHashMap<>();

    public static final String RATE_LIMITER_PREFIX = "rateLimiter";

    private BucketConfiguration buildConfig(RateLimiter limit) {
        final int capacity = limit.capacity();

        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(
                        capacity,
                        Duration.ofSeconds(limit.durationInSeconds())
                )
                .build();

        return BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }

    @Around("@annotation(rateLimiter)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        final String identifier = String.format(
                "%s-%s",
                getUniqueId(request),
                request.getRequestURI()
        );

        final String key = generateRedisKey(identifier);

        Bucket bucket = lettuceBasedProxyManager
                .getProxy(key, () -> bucketConfigCache.computeIfAbsent(rateLimiter, this::buildConfig));

        if (!bucket.tryConsume(1)) {
            throw new DomainException(RateLimiterException.EXCEEDED);
        }

        return joinPoint.proceed();
    }

    private String generateRedisKey(String uniqueKey){
        return String.format("%s:%s", RATE_LIMITER_PREFIX, uniqueKey);
    }

    @Nullable
    private String getUniqueId(HttpServletRequest request) {
        var uniqueId = request.getAttribute(UniqueIdFilter.UNIQUE_ID_HEADER);

        if(uniqueId == null){
            return null;
        }

        return uniqueId.toString();
    }
}
