package org.glitchproof.auth.features.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.auth.enums.MagicLinkStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.auth.event.MagicLinkEvent;
import org.glitchproof.auth.features.auth.enums.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.glitchproof.auth.features.token.service.JwtService;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.auth.service.MagicLinkService;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.auth.dto.magic_link.MagicLinkRequest;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MagicLinkServiceImpl
        implements MagicLinkService {
    private final JwtService jwtService;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${app.magic-link.url}")
    private String magicLinkBaseUrl;

    @Override
    public void send(MagicLinkRequest magicLinkRequest) {
        final String email = magicLinkRequest.email();

        if(!userService.existsByEmail(email)){
            throw new DomainException(AuthException.MAGIC_LINK_ONLY_SEND_REGISTERED);
        }

        final Long fifteenMinutes = 15 * 60 * 1000L;

        final String magicToken = jwtService.generateToken(
                email,
                null,
                fifteenMinutes,
                TokenType.MAGIC
        );

        String link = String.format(
                "%s?token=%s",
                magicLinkBaseUrl,
                magicToken
        );

        applicationEventPublisher.publishEvent(
                new MagicLinkEvent(
                        this,
                        email,
                        link
                )
        );

        log.info("Magic link send to email: {}", email);
    }

    @Override
    public TokenResponse validate(String token) {
        if(!jwtService.validate(token, TokenType.MAGIC)){
            throw new DomainException(AuthException.MAGIC_LINK_INVALID);
        }

        final String email = jwtService.getSubjectFromToken(token);

        final String status = redisTemplate.opsForValue().get(email);

        final MagicLinkStatus magicLinkStatus = MagicLinkStatus.valueOf(status);

        if(magicLinkStatus.equals(MagicLinkStatus.USED)){
            throw new DomainException(AuthException.MAGIC_LINK_USED);
        }

        if(!userService.existsByEmail(email)){
            throw new DomainException(UserException.EMAIL_NOT_FOUND);
        }

        userService.updateLastLogin(email);

        redisTemplate.opsForValue().set(
                email,
                MagicLinkStatus.USED.name(),
                5,
                TimeUnit.MINUTES
        );

        log.info("Magic link validated: {}", email);

        return jwtService.generatePairToken(email);
    }
}
