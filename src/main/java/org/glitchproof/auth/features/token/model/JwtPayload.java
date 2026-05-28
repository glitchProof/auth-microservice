package org.glitchproof.auth.features.token.model;

import java.util.Map;

public record JwtPayload(
        String email,
        Map<String, String> claims
) {}
