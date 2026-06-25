package org.glitchproof.auth.features.auth.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.glitchproof.auth.features.auth.dto.RegisterRequest;

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatch, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        var isValid = registerRequest.getPassword().equals(registerRequest.getPasswordConfirm());

        if(!isValid){
            context.disableDefaultConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("passwordConfirm")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
