package org.glitchproof.auth.features.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@Schema(description = "user creation request")
public class CreateUserRequest {
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
}