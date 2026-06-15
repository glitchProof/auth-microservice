package org.glitchproof.auth.features.auth.dto;

import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Schema(example = "Jhon Doe")
    String fullName;

    @NotBlank
    @Schema(example = "Named_king")
    String username;

    @NotBlank
    @Schema(example = "WriteBetterPassword123")
    String password;

    @NotBlank
    @Email
    @Schema(example = "namedking@gmail.com")
    String email;

    @Schema(
            description = "Confirmation of password",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String passwordConfirm;
}
