package org.glitchproof.auth.features.auth.exception;

import lombok.RequiredArgsConstructor;

import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;

import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

import java.util.Map;

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
            "Passwords confirm do not match password",
            Map.of(
                    "passwordConfirm", "Password confirm not match with password"
            )
    )),
    OAUTH_TOKEN_NOT_VALID(of(
               HttpStatus.UNAUTHORIZED,
               10114,
               "Token is invalid or expired"
    )),
    MAGIC_LINK_ONLY_SEND_REGISTERED(of(
                    HttpStatus.NOT_FOUND,
                    10115,
                    "Magic link only work registered user's"
    )),
    MAGIC_LINK_INVALID(of(
            HttpStatus.BAD_REQUEST,
            10116,
            "Magic link invalid"
    )),
    MAGIC_LINK_USED(of(
            HttpStatus.BAD_REQUEST,
            10117,
            "Magic link used"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
