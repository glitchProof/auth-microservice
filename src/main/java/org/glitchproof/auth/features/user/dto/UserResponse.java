package org.glitchproof.auth.features.user.dto;

import lombok.Builder;
import org.glitchproof.auth.features.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        LocalDateTime lastLogin
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
