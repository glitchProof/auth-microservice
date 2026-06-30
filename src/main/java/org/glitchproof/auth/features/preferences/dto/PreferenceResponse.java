package org.glitchproof.auth.features.preferences.dto;

import lombok.Builder;
import org.glitchproof.auth.features.preferences.entity.Preferences;
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
) {
    public static PreferenceResponse from(Preferences preferences) {
        return PreferenceResponse.builder()
                .language(preferences.getLanguage())
                .theme(preferences.getTheme())
                .timezone(preferences.getTimezone())
                .notificationEnabled(preferences.getNotificationEnabled())
                .emailNotificationEnabled(preferences.getEmailNotificationEnabled())
                .pushNotificationEnabled(preferences.getPushNotificationEnabled())
                .build();
    }
}
