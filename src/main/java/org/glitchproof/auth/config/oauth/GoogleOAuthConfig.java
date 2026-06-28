package org.glitchproof.auth.config.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.time.Duration;
import java.util.List;

@Configuration
public class GoogleOAuthConfig {
    private final static Duration TIMEOUT = Duration.ofSeconds(2);

    @Bean
    public NimbusJwtDecoder googleNimbusJwtDecoder(
            @Value("${app.oauth.google.client-id}") String clientId,
            @Value("${app.oauth.google.issuer-uri}") String issuerUri,
            @Value("${app.oauth.google.jwk-set-uri}") String jwkSetUri
    ){
        var nimbusDecoder = NimbusJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .restOperations(restOperations())
                .build();

        configureNimbusDecoder(clientId, issuerUri, nimbusDecoder);

        return nimbusDecoder;
    }


    private RestTemplate restOperations(){
        var restTemplate = new RestTemplate();
        var httpFactory = new SimpleClientHttpRequestFactory();

        httpFactory.setConnectTimeout(TIMEOUT);
        httpFactory.setReadTimeout(TIMEOUT);

        restTemplate.setRequestFactory(httpFactory);

        return restTemplate;
    };


    private void configureNimbusDecoder(
            String clientId,
            String issuerUri,
            NimbusJwtDecoder nimbusDecoder
    ){
        var delegationValidator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                jwtAudienceValidation(clientId),
                issuerValidator(issuerUri)
        );

        nimbusDecoder.setJwtValidator(delegationValidator);
    }

    private OAuth2TokenValidator<Jwt> jwtAudienceValidation(String clientId){
        return new JwtClaimValidator<List<String>>(
                JwtClaimNames.AUD,
                aud -> aud != null && aud.contains(clientId)
        );
    };

    private OAuth2TokenValidator<Jwt> issuerValidator(String issuerUri) {
        final String bareIssuer = issuerUri.replaceFirst("^https?://", "");

        return token -> {
            Object iss = token.getClaim(JwtClaimNames.ISS);
            String issuer = (iss == null) ? null : iss.toString();

            if (issuerUri.equals(issuer) || bareIssuer.equals(issuer)) {
                return OAuth2TokenValidatorResult.success();
            }

            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error(
                            "invalid_issuer",
                            "The iss claim is not valid",
                            null
                    )
            );
        };
    }


}
