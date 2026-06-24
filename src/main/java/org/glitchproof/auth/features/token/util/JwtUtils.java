package org.glitchproof.auth.features.token.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.auth.enums.TokenType;
import org.glitchproof.auth.features.token.exception.JwtException;
import org.glitchproof.auth.features.token.service.JwtService;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtService jwtService;

    @Value("${app.jwt.access-token-expiration}")
    private Long jwtAccessExpireTime;

    @Value("${app.jwt.refresh-token-expiration}")
    private Long jwtRefreshExpireTime;

    private final Supplier<Exception> defaultExceptionSupplier =
            () -> new DomainException(JwtException.INVALID);

    public void validateOrThrow(
            String token,
            TokenType type,
            Supplier<Exception> throwCallback
    ) throws Exception {
        try {
            var isValid = jwtService.validate(token, type);

            if(!isValid) {
                throw new DomainException(JwtException.EXPIRED);
            }
        } catch (DomainException e) {
            throw e;
        } catch(io.jsonwebtoken.JwtException e) {
            JwtException error = switch(e){
                case ExpiredJwtException _e -> JwtException.EXPIRED;
                case MalformedJwtException _e -> JwtException.MALFORMED;
                default -> JwtException.INVALID;
            };

            throw new DomainException(error);
        } catch (Exception e) {

            log.error("JWT validation failed: {}", e.getMessage());

            throw throwCallback.get();
        }
    }

    public String validateAndGetSubject(
            String token,
            TokenType type,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, type, throwCallback);

        return jwtService.getSubjectFromToken(token);
    }

    // Refresh token validators
    public void validateRefreshToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.REFRESH, defaultExceptionSupplier);
    }

    public String generateRefreshToken(String email){
        return jwtService.generateToken(email, null, jwtRefreshExpireTime, TokenType.REFRESH);
    }

    public String generateRefreshToken(String email, Map<String, String> claims){
        return jwtService.generateToken(email, claims, jwtRefreshExpireTime, TokenType.REFRESH);
    }

    public void validateRefreshToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.REFRESH, throwCallback);
    }

    public String validateAndGetSubjectFromRefreshToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.REFRESH, throwCallback);

        return jwtService.getSubjectFromToken(token);
    }

    public String validateAndGetSubjectFromRefreshToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.REFRESH, defaultExceptionSupplier);

        return jwtService.getSubjectFromToken(token);
    }

    // Magic token validators
    public void validateMagicToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.MAGIC, defaultExceptionSupplier);
    }

    public void validateMagicToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.MAGIC, throwCallback);
    }

    public String validateAndGetSubjectFromMagicToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.MAGIC, throwCallback);

        return jwtService.getSubjectFromToken(token);
    }

    public String validateAndGetSubjectFromMagicToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.MAGIC, defaultExceptionSupplier);

        return jwtService.getSubjectFromToken(token);
    }

    // Access token validators
    public String generateAccessToken(String email){
        return jwtService.generateToken(email, null, jwtAccessExpireTime, TokenType.ACCESS);
    }

    public String generateAccessToken(String email, Map<String, String> claims){
        return jwtService.generateToken(email, claims, jwtAccessExpireTime, TokenType.ACCESS);
    }

    public void validateAccessToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.ACCESS, defaultExceptionSupplier);
    }

    public void validateAccessToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.ACCESS, throwCallback);
    }

    public String validateAndGetSubjectFromAccessToken(
            String token,
            Supplier<Exception> throwCallback
    ) throws Exception {
        validateOrThrow(token, TokenType.ACCESS, throwCallback);

        return jwtService.getSubjectFromToken(token);
    }

    public String validateAndGetSubjectFromAccessToken(
            String token
    ) throws Exception {
        validateOrThrow(token, TokenType.ACCESS, defaultExceptionSupplier);

        return jwtService.getSubjectFromToken(token);
    }

    // common
    public TokenResponse generatePairToken(String email){
        return new TokenResponse(generateAccessToken(email), generateRefreshToken(email));
    }

}
