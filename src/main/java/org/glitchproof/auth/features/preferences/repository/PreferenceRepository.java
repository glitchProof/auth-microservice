package org.glitchproof.auth.features.preferences.repository;

import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreferenceRepository extends JpaRepository<Preferences, UUID> {
    @Query("select p from Preferences p join fetch p.user u where u.email = :email")
    Optional<Preferences> findByUserEmail(String email);
}
