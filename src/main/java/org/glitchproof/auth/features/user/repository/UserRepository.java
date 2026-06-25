package org.glitchproof.auth.features.user.repository;

import org.glitchproof.auth.features.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
}
