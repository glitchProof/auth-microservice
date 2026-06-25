package org.glitchproof.auth.config.security;

import org.glitchproof.auth.features.user.model.CustomUserDetails;

import java.util.UUID;

@FunctionalInterface
public interface JwtDetailedUserResolver {
    CustomUserDetails fetchUser(UUID userID);
}
