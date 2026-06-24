package org.glitchproof.auth.features.preferences.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.mapper.PreferenceMapper;
import org.glitchproof.auth.features.preferences.repository.PreferenceRepository;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl
        implements PreferenceService {
    private final PreferenceMapper preferenceMapper;
    private final PreferenceRepository preferenceRepository;

    @Override
    public PreferenceResponse getPreference(String email) {
        return preferenceRepository
                .findByUserEmail(email)
                .map(preferenceMapper::preferenceToPreferenceResponse)
                .orElseThrow(() -> new DomainException(UserException.EMAIL_NOT_FOUND));
    }
}
