package org.glitchproof.auth.features.token.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;


@RequiredArgsConstructor
public enum JwtException implements ExceptionDefinition {
    INVALID(of(
            HttpStatus.UNAUTHORIZED,
            10101,
            "Jwt is invalid"
    )),
    MALFORMED(of(
                    HttpStatus.UNAUTHORIZED,
                    10102,
                    "Jwt is malformed"
    )),
    EXPIRED(of(
                    HttpStatus.UNAUTHORIZED,
                    10103,
                    "Jwt is expired"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
