package org.glitchproof.auth.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;
import org.glitchproof.auth.core.model.DomainExceptionHolder;

import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;

@Getter
@RequiredArgsConstructor
public enum DomainExceptions implements ExceptionDefinition {
    SOMETHING_WRONG(
            of(
                    HttpStatus.BAD_REQUEST,
                    DomainErrorCodes.SOMETHING_WENT_WRONG,
                    "Something went wrong"
            )
    );

    private final DomainExceptionHolder domainExceptionHolder;

}
