package org.glitchproof.auth.features.user.model;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.glitchproof.auth.features.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(User user) implements UserDetails {
    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_GUEST")
        );
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    @NonNull
    public String getUsername() {
        return user.getEmail();
    }
}
