package org.glitchproof.auth.features.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.auth.dto.oauth.GoogleUserDto;
import org.glitchproof.auth.features.auth.service.GoogleTokenVerificationService;

import java.util.Objects;

@Slf4j
@Service
public class GoogleTokenVerificationServiceImpl
        implements GoogleTokenVerificationService {
    private final NimbusJwtDecoder jwtDecoder;
    private final String expectedClientId;

    public GoogleTokenVerificationServiceImpl(
            @Value("${app.oauth.google.client-id}") String expectedClientId,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri
    ){
        this.expectedClientId = expectedClientId;
        this.jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }


    public GoogleUserDto verify(TokenRequest tokenRequest){
      try {
          Jwt jwt = jwtDecoder.decode(tokenRequest.token());

          if (!Objects.requireNonNull(jwt.getAudience()).contains(expectedClientId)) {
              throw new DomainException(AuthException.OAUTH_TOKEN_NOT_VALID);
          }

          return new GoogleUserDto(
                  jwt.getSubject(),
                  jwt.getClaimAsString("email"),
                  jwt.getClaimAsString("name"),
                  jwt.getClaimAsString("picture")
          );
      } catch (DomainException e){
          throw e;
      } catch (Exception e) {
          log.error("Google token verification failed", e);

          throw new DomainException(AuthException.OAUTH_TOKEN_NOT_VALID);
      }
    }
}
