package org.glitchproof.auth.features.preferences.services;

import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.glitchproof.auth.features.user.entity.User;

import java.util.UUID;

public interface  PreferenceService {
    PreferenceResponse getPreference(User authenticatedUser);
    PreferenceResponse updatePreference(User authenticatedUser, PreferenceUpdateRequest updatedPreference);


    InternalService internal();

    interface InternalService {
        Preferences getPreference(UUID userID);
    }

}
