package org.glitchproof.auth.features.preferences.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.mapper.PreferenceMapper;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.glitchproof.auth.features.preferences.repository.PreferenceRepository;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl
        implements PreferenceService {
    private final PreferenceMapper preferenceMapper;
    private final PreferenceRepository preferenceRepository;

    @Override
    public PreferenceResponse getPreference(User authenticatedUser) {
        var preference = preferenceRepository.findByUserId(authenticatedUser.getId());

        return preferenceMapper.preferenceToPreferenceResponse(preference);
    }

    @Override
    public PreferenceResponse updatePreference(User authenticatedUser, PreferenceUpdateRequest updatedPreference) {
        var preference = authenticatedUser.getPreferences();

        preferenceMapper.updatePreference(updatedPreference, preference);

        var savedPreference = preferenceRepository.save(preference);

        return preferenceMapper
                .preferenceToPreferenceResponse(savedPreference);
    }
}
