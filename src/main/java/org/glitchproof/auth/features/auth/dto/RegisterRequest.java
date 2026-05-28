package org.glitchproof.auth.features.auth.dto;

import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "auth register request")
public class RegisterRequest {
    @NotBlank
    @Schema(examples = "Jhon Doe")
    String fullName;

    @NotBlank
    @Schema(examples = "Named_king")
    String username;

    @NotBlank
    @Schema(examples = "WriteBetterPassword123")
    String password;

    @NotBlank
    @Email
    @Schema(examples = "namedking@gmail.com")
    String email;

    @Schema(
            description = "Confirmation of password",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String passwordConfirm;
}