package org.glitchproof.auth.features.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glitchproof.auth.features.auth.validator.PasswordMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatch
public class RegisterRequest {
    @NotBlank
    @Schema(example = "Jhon Doe")
    String fullName;

    @NotBlank
    @Schema(example = "Named_king")
    String username;

    @NotBlank
    @Schema(example = "WriteBetterPassword123")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be valid pattern"
    )
    String password;

    @NotBlank
    @Email
    @Schema(example = "namedking@gmail.com")
    String email;

    @NotBlank
    @Schema(
            description = "Confirmation of password",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String passwordConfirm;
}
