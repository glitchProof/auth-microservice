package org.glitchproof.auth.features.preferences.services;

import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;

public interface  PreferenceService {
    PreferenceResponse getPreference(String email);
    PreferenceResponse updatePreference(String email, PreferenceUpdateRequest updatedPreference);
}
