package org.glitchproof.auth.features.preferences.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.enums.DomainErrorCodes;
import org.glitchproof.auth.core.model.DomainExceptionHolder;
import static org.glitchproof.auth.core.model.DomainExceptionHolder.of;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PreferenceException implements ExceptionDefinition {
    PREFERENCE_NOT_FOUND(of(
            HttpStatus.BAD_REQUEST,
            DomainErrorCodes.PREFERENCES_NOT_FOUND,
            "Preferences not found"
    ));

   public final DomainExceptionHolder domainExceptionHolder;
}
