package org.glitchproof.auth.features.preferences.services;

import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.glitchproof.auth.features.user.entity.User;

public interface  PreferenceService {
    PreferenceResponse getPreference(User authenticatedUser);
    PreferenceResponse updatePreference(User authenticatedUser, PreferenceUpdateRequest updatedPreference);
}
