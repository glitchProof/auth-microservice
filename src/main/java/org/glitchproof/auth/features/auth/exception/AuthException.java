package org.glitchproof.auth.features.auth.exception;

import lombok.RequiredArgsConstructor;

import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;
import static org.glitchproof.auth.core.enums.DomainErrorCodes.*;

import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RequiredArgsConstructor
public enum AuthException implements ExceptionDefinition {
    CREDENTIALS_NOT_VALID(of(
            HttpStatus.BAD_REQUEST,
            AUTH_CREDENTIALS_NOT_VALID,
            "Email or password invalid credentials"
    )),
    EMAIL_ALREADY_TAKEN(of(
            HttpStatus.CONFLICT,
            AUTH_EMAIL_TAKEN,
            "Email already taken"
    )),
    USERNAME_ALREADY_TAKEN(of(
            HttpStatus.CONFLICT,
            AUTH_USERNAME_TAKEN,
            "Username already taken"
    )),
    PASSWORD_CONFIRM_NOT_MATCH(of(
            HttpStatus.BAD_REQUEST,
            AUTH_PASSWORD_MISMATCH,
            "Passwords confirm do not match password",
            Map.of("passwordConfirm", "Password confirm not match with password")
    )),
    OAUTH_TOKEN_NOT_VALID(of(
            HttpStatus.UNAUTHORIZED,
            AUTH_OAUTH_TOKEN_INVALID,
            "Token is invalid or expired"
    )),
    MAGIC_LINK_INVALID(of(
            HttpStatus.BAD_REQUEST,
            AUTH_MAGIC_LINK_INVALID,
            "Magic link invalid"
    )),
    MAGIC_LINK_USED(of(
            HttpStatus.BAD_REQUEST,
            AUTH_MAGIC_LINK_USED,
            "Magic link used"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
