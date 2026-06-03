package org.glitchproof.auth.features.auth.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        @Schema(
                example = "loong loong token"
        )
        String token
) {}
