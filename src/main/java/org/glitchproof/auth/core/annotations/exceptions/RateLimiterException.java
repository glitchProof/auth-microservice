package org.glitchproof.auth.core.annotations.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;
import static org.glitchproof.auth.core.enums.DomainErrorCodes.*;

@Getter
@RequiredArgsConstructor
public enum RateLimiterException implements ExceptionDefinition {
    EXCEEDED(of(
            HttpStatus.TOO_MANY_REQUESTS,
            AUTH_RATE_LIMIT_EXCEEDED,
            "Too many request limit exceeded"
    ));

    private final DomainExceptionHolder domainExceptionHolder;
}
