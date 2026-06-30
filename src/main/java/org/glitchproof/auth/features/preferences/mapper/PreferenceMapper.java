package org.glitchproof.auth.features.preferences.mapper;

import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.mapstruct.*;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.mapstruct.control.MappingControl;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PreferenceMapper {
    void updatePreference(PreferenceUpdateRequest updateRequest, @MappingTarget Preferences preference);
}
