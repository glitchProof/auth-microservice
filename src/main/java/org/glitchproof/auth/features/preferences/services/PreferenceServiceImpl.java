package org.glitchproof.auth.features.preferences.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.transaction.annotation.Transactional;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.glitchproof.auth.features.preferences.exceptions.PreferenceException;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.mapper.PreferenceMapper;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.glitchproof.auth.features.preferences.repository.PreferenceRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl
        implements PreferenceService {
    private final PreferenceMapper preferenceMapper;
    private final PreferenceRepository preferenceRepository;

    @Override
    @Transactional(readOnly = true)
    public PreferenceResponse getPreference(User authenticatedUser) {
        var preference = authenticatedUser.getPreferences();

        return PreferenceResponse.from(preference);
    }

    @Override
    @Transactional
    public PreferenceResponse updatePreference(User authenticatedUser, PreferenceUpdateRequest updatedPreference) {
        var preference = authenticatedUser.getPreferences();

        preferenceMapper.updatePreference(updatedPreference, preference);

        var savedPreference = preferenceRepository.save(preference);

        return PreferenceResponse.from(savedPreference);
    }

    public InternalService internal() {
        return new InternalService();
    }

    public class InternalService
            implements PreferenceService.InternalService {

        @Override
        public Preferences getPreference(UUID userID) {
            return preferenceRepository
                    .findById(userID)
                    .orElseThrow(() -> new DomainException(PreferenceException.PREFERENCE_NOT_FOUND));
        }
    }

}
