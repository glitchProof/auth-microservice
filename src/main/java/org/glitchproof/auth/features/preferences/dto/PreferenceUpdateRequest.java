package org.glitchproof.auth.features.preferences.dto;

import lombok.Builder;
import org.glitchproof.auth.features.preferences.enums.Languages;
import org.glitchproof.auth.features.preferences.enums.ThemeMode;

@Builder
public record PreferenceUpdateRequest(
        ThemeMode   theme,
        Languages   language,
        String      timezone,
        Boolean     notificationEnabled,
        Boolean     emailNotificationEnabled,
        Boolean     pushNotificationEnabled
) {}
