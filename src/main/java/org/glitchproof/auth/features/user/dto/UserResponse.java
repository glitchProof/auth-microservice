package org.glitchproof.auth.features.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        String username,
        String fullName,
        String email,
        LocalDateTime lastLogin
) {}
