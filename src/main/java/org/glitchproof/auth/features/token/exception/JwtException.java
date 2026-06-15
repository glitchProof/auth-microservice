package org.glitchproof.auth.features.token.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;
import static org.glitchproof.auth.core.enums.DomainErrorCodes.*;

@RequiredArgsConstructor
public enum JwtException implements ExceptionDefinition {
    INVALID(of(
            HttpStatus.UNAUTHORIZED,
            JWT_INVALID,
            "Jwt is invalid"
    )),
    MALFORMED(of(
            HttpStatus.UNAUTHORIZED,
            JWT_MALFORMED,
            "Jwt is malformed"
    )),
    EXPIRED(of(
            HttpStatus.UNAUTHORIZED,
            JWT_EXPIRED,
            "Jwt is expired"
    ));

    private final DomainExceptionHolder domainExceptionHolder;

    @Override
    public DomainExceptionHolder getDomainExceptionHolder() {
        return domainExceptionHolder;
    }
}
