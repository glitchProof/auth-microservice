package org.glitchproof.auth.features.preferences.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PreferenceMapper {
    PreferenceResponse preferenceToPreferenceResponse(Preferences preferences);
}
