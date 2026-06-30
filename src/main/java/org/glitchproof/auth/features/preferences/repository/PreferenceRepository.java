package org.glitchproof.auth.features.preferences.repository;

import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreferenceRepository extends JpaRepository<Preferences, UUID> {

}
