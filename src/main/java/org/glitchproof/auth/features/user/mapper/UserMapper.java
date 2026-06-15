package org.glitchproof.auth.features.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.dto.UserResponse;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    User createUserRequestToUserEntity(CreateUserDto createUserDto);

    UserResponse userEntityToUserResponse(User user);

    void upsertUser(UpsertUserDto upsertUserDto, @MappingTarget User user);
}
