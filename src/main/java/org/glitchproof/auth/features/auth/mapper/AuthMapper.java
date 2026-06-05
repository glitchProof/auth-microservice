package org.glitchproof.auth.features.auth.mapper;

import org.glitchproof.auth.features.auth.dto.RegisterRequest;
import org.glitchproof.auth.features.auth.dto.oauth.GoogleUserDto;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper {

    @Mapping(target = "googleSubId", source = "subId")
    @Mapping(target = "username", source = "fullName")
    CreateUserDto toCreateUserRequest(GoogleUserDto googleUserDto);

    CreateUserDto registerRequestToCreateUserRequest(RegisterRequest registerRequest);
}
