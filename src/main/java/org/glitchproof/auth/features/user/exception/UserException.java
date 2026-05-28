package org.glitchproof.auth.features.user.exception;

import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserException implements ExceptionDefinition {
    USERNAME_AND_EMAIL_EXISTS(
            of(
                    HttpStatus.CONFLICT,
                    10120,
                    "Username or email already exists in db"
            )
    ),
    EMAIL_NOT_FOUND(of(
            HttpStatus.BAD_REQUEST,
            10201,
            "Email not found in db"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
