package org.glitchproof.auth.features.auth.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.auth.enums.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.glitchproof.auth.features.token.service.JwtService;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.event.MagicLinkEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.glitchproof.auth.features.auth.enums.MagicLinkStatus;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.auth.service.MagicLinkService;
import org.glitchproof.auth.features.auth.dto.magic_link.MagicLinkRequest;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MagicLinkServiceImpl
        implements MagicLinkService {
    private final JwtUtils jwtUtils;
    private final JwtService jwtService;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final long FIFTEEN_MINUTES = TimeUnit.MINUTES.toMillis(15);

    @Value("${app.magic-link.url}")
    private String magicLinkBaseUrl;

    @Override
    public void send(MagicLinkRequest magicLinkRequest) {
        final String email = magicLinkRequest.email();

        if(!userService.existsByEmail(email)){
            log.warn("Magic link can not be sent to {}, email not found", email);

            return;
        }

        final String magicToken = generateMagicToken(email);
        final String magicKey = magicLinkKey(email, magicToken);
        final String magicLink = generateMagicLink(magicToken);

        redisTemplate
                .opsForValue()
                .set(
                        magicKey,
                        MagicLinkStatus.UNUSED.name(),
                        15,
                        TimeUnit.MINUTES
                );

        applicationEventPublisher.publishEvent(
                new MagicLinkEvent(this, email, magicLink)
        );

        log.info("magic link created for user {} and event dispatched", email);
    }

    @Override
    @SneakyThrows
    public TokenResponse validate(String token) {
        final var email = jwtUtils.validateAndGetSubjectFromAccessToken(token);

        String magicLinkKey = magicLinkKey(email, token);

        String status = redisTemplate.opsForValue().get(magicLinkKey);

        if(status == null){
            throw new DomainException(AuthException.MAGIC_LINK_INVALID);
        }

        final MagicLinkStatus magicLinkStatus = MagicLinkStatus.valueOf(status);

        if(magicLinkStatus.equals(MagicLinkStatus.USED)){
            throw new DomainException(AuthException.MAGIC_LINK_USED);
        }

        if(!userService.existsByEmail(email)){
            throw new DomainException(UserException.EMAIL_NOT_FOUND);
        }

        userService.updateLastLogin(email);

        redisTemplate.opsForValue().set(
                magicLinkKey,
                MagicLinkStatus.USED.name(),
                5,
                TimeUnit.MINUTES
        );

        log.info("Magic link validated: {}", email);

        return jwtUtils.generatePairToken(email);
    }

    private String magicLinkKey(String email, String token) {
        return String.format("magic-link:%s-%s", email, token);
    }

    private String generateMagicToken(String email) {
        return jwtService.generateToken(email, null, FIFTEEN_MINUTES, TokenType.MAGIC);
    }

    private String generateMagicLink(String magicToken) {
        return String.format(
                "%s?token=%s",
                magicLinkBaseUrl,
                magicToken
        );
    }
}
