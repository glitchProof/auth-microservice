package org.glitchproof.auth.features.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.*;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.auth.dto.oauth.GoogleUserDto;
import org.glitchproof.auth.features.auth.service.GoogleTokenVerificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenVerificationServiceImpl
        implements GoogleTokenVerificationService {
    private final NimbusJwtDecoder googleNimbusJwtDecoder;

    public GoogleUserDto verify(TokenRequest tokenRequest) {
        final Jwt jwt;

        try {
            jwt = googleNimbusJwtDecoder.decode(tokenRequest.token());
        } catch (BadJwtException e) {
            log.warn("Google ID token rejected: {}", e.getMessage());

            throw new DomainException(AuthException.OAUTH_TOKEN_NOT_VALID);
        } catch (JwtException e) {
            log.error("Google token verification unavailable (JWKS/infrastructure error)", e);

            throw e;
        }

        if (!Boolean.TRUE.equals(jwt.getClaimAsBoolean("email_verified"))) {
            throw new DomainException(AuthException.OAUTH_TOKEN_NOT_VALID);
        }

        return new GoogleUserDto(
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("name"),
                jwt.getClaimAsString("picture"),
                jwt.getClaimAsBoolean("email_verified")
        );
    }
}
