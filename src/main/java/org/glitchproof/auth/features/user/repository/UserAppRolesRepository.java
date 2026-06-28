package org.glitchproof.auth.features.user.repository;

import org.springframework.stereotype.Repository;
import org.glitchproof.auth.features.user.entity.UserAppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAppRolesRepository extends JpaRepository<UserAppRole, UUID> {
    List<UserAppRole> findByUserId(UUID userId);
}
