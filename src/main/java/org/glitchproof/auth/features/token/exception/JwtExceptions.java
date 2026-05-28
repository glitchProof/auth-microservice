package org.glitchproof.auth.features.token.exception;

import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum JwtExceptions implements ExceptionDefinition {
    INVALID(DomainExceptionHolder.of(
            HttpStatus.UNAUTHORIZED,
            10101,
            "Jwt is invalid"
    )),
    MALFORMED(
            DomainExceptionHolder.of(
                    HttpStatus.UNAUTHORIZED,
                    10102,
                    "Jwt is malformed"
            )
    ),
    EXPIRED(
            DomainExceptionHolder.of(
                    HttpStatus.UNAUTHORIZED,
                    10103,
                    "Jwt is expired"
            )
    );

    private final DomainExceptionHolder domainExceptionHolder;


    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
