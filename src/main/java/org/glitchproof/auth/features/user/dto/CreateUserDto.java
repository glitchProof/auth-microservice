package org.glitchproof.auth.features.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glitchproof.auth.features.user.enums.AuthProvider;
import org.glitchproof.auth.features.user.validators.UniqueEmail;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "user creation request")
public class CreateUserDto {
    @NotBlank
    String fullName;

    @NotBlank
    String username;

    @NotBlank
    @ToString.Exclude
    String password;

    @NotBlank
    @Email
    @UniqueEmail
    String email;

    String googleSubId;

    AuthProvider provider;

    @Builder.Default
    Boolean emailVerified = false;
}
