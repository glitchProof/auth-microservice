package org.glitchproof.auth.features.auth.exception;

import lombok.RequiredArgsConstructor;

import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;

import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthException implements ExceptionDefinition {
    CREDENTIALS_NOT_VALID(of(
            HttpStatus.BAD_REQUEST,
            10110,
            "Email or password invalid credentials"
    )),
    EMAIL_ALREADY_TAKEN(of(
            HttpStatus.CONFLICT,
            10112,
            "Email already taken"
    )),
    USERNAME_ALREADY_TAKEN(of(
            HttpStatus.CONFLICT,
            10111,
            "Username already taken"
    )),
    PASSWORD_CONFIRM_NOT_MATCH(of(
            HttpStatus.BAD_REQUEST,
            10113,
            "Passwords confirm do not match password"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
