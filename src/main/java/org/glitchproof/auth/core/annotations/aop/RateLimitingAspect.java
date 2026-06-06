package org.glitchproof.auth.core.annotations.aop;

import java.util.Map;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.glitchproof.auth.core.annotations.RateLimiter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class RateLimitingAspect {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket(RateLimiter limit) {
        final var capacity = limit.capacity();
        final var durationInSeconds = Duration.ofSeconds(limit.durationInSeconds());

        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(capacity, durationInSeconds)
                .build();

        return Bucket
                .builder()
                .addLimit(bandwidth)
                .build();
    }


    @Around("@annotation(rateLimiter)")
    public Object enforceRateLimit(
            ProceedingJoinPoint joinPoint,
            RateLimiter rateLimiter
    ) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String clientIp = request.getRemoteAddr();
        String apiPath = request.getRequestURI();
        String cacheKey = clientIp + ":" + apiPath;

        Bucket bucket = cache.computeIfAbsent(cacheKey, k -> createNewBucket(rateLimiter));

        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many requests - endpoint limit exceeded."
            );
        }


        return joinPoint.proceed();
    }
}
