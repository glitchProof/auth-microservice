package org.glitchproof.auth.features.auth.dto.credentials;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordAuthRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
//        @Pattern(
//                regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,}",
//                message = "Password must be valid pattern"
//        )
        String password
) {}
