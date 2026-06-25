package org.glitchproof.auth.features.auth.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.event.MagicLinkEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.glitchproof.auth.features.auth.enums.MagicLinkStatus;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.auth.service.MagicLinkService;
import org.glitchproof.auth.features.auth.dto.magic_link.MagicLinkRequest;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MagicLinkServiceImpl
        implements MagicLinkService {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${app.magic-link.url}")
    private String magicLinkBaseUrl;

    @Override
    public void send(MagicLinkRequest magicLinkRequest) {
        final String email = magicLinkRequest.email();

        final var user = userService
                .internal()
                .getUserByEmail(email);

        if(user == null || !user.getEmailVerified()){
            log.warn("Magic link can not be sent to {}, email not found", email);

            return;
        }

        final var userID = user.getId();

        final String magicToken = jwtUtils.generateMagicToken(userID);
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
        final var userID = jwtUtils
                .validateAndGetSubjectFromMagicToken(token);

        final var user = userService
                .internal()
                .getUserById(userID);

        final var email = user.getEmail();

        String magicLinkKey = magicLinkKey(email, token);

        final var status = redisTemplate
                .opsForValue()
                .getAndDelete(magicLinkKey);

        if(status == null){
            throw new DomainException(AuthException.MAGIC_LINK_INVALID);
        }

        final MagicLinkStatus magicLinkStatus = MagicLinkStatus.valueOf(status);

        if(magicLinkStatus.equals(MagicLinkStatus.USED)){
            throw new DomainException(AuthException.MAGIC_LINK_USED);
        }

        userService.updateLastLogin(email);

        redisTemplate
                .opsForValue()
                .setIfPresent(
                        magicLinkKey,
                        MagicLinkStatus.USED.name(),
                        5,
                        TimeUnit.MINUTES
                );

        log.info("Magic link validated: {}", email);

        return jwtUtils.generatePairToken(user.getId());
    }

    private String magicLinkKey(String email, String token) {
        return String.format("magic-link:%s-%s", email, token);
    }

    private String generateMagicLink(String magicToken) {
        return String.format(
                "%s?token=%s",
                magicLinkBaseUrl,
                magicToken
        );
    }
}
