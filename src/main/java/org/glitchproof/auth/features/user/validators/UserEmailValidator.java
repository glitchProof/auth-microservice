package org.glitchproof.auth.features.user.validators;

import lombok.RequiredArgsConstructor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.glitchproof.auth.features.user.service.UserService;

@RequiredArgsConstructor
public class UserEmailValidator
        implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.existsByEmail(email);
    }
}
