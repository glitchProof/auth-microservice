package org.glitchproof.auth.features.user.dto;

import lombok.Builder;
import org.glitchproof.auth.features.user.enums.AuthProvider;

import java.time.LocalDateTime;

@Builder
public record UpsertUserDto(
   String fullName,
   String username,
   String googleSubId,
   AuthProvider provider,
   LocalDateTime lastLogin
) {}
