package org.glitchproof.auth.features.auth.dto.oauth;

public record GoogleUserDto(
        String subId,
        String email,
        String fullName,
        String picture
) {}
