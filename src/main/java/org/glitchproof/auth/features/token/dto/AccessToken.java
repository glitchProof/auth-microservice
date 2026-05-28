package org.glitchproof.auth.features.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(
        @JsonProperty("access_token")
        String accessToken
) {}
