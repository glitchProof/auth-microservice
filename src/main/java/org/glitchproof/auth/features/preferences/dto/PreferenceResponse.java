package org.glitchproof.auth.features.preferences.dto;

import lombok.Builder;
import org.glitchproof.auth.features.preferences.enums.Languages;
import org.glitchproof.auth.features.preferences.enums.ThemeMode;

@Builder
public record PreferenceResponse(
        Languages   language,
        ThemeMode   theme,
        String      timezone,
        Boolean     notificationEnabled,
        Boolean     emailNotificationEnabled,
        Boolean     pushNotificationEnabled
) {}
