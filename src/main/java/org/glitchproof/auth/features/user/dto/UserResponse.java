package org.glitchproof.auth.features.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        LocalDateTime lastLogin
) {}
