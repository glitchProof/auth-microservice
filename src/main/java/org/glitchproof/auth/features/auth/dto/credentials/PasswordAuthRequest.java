package org.glitchproof.auth.features.auth.dto.credentials;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record PasswordAuthRequest(
        @NotBlank
        @Email
        String email,

        @NotNull
        @NotBlank
//        @Pattern(
//                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,}$",
//                message = "Password must be valid pattern"
//        )
        @Schema(example = "StrongPassword123@123")
        String password
) {}
