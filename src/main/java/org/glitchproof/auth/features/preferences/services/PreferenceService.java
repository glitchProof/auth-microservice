package org.glitchproof.auth.features.preferences.services;

import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;

public interface PreferenceService {
    PreferenceResponse getPreference(String email);
}
