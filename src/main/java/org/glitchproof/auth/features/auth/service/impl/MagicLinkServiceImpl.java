package org.glitchproof.auth.features.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.core.event.MagicLinkEvent;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MagicLinkServiceImpl
        implements MagicLinkService {
    private final JwtService jwtService;
    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${magic-link.base-url}")
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
                "%s/api/v1/auth/login/magic-link/verify?token=%s",
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

        if(!userService.existsByEmail(email)){
            throw new DomainException(UserException.EMAIL_NOT_FOUND);
        }

        log.info("Magic link validated: {}", email);

        return jwtService.generatePairToken(email);
    }
}
