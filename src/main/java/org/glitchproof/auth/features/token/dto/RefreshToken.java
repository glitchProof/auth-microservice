package org.glitchproof.auth.features.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshToken(
        @JsonProperty("refresh_token")
        String refreshToken
) {}
