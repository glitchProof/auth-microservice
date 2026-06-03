package org.glitchproof.auth.features.user.mapper;


import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User createUserRequestToUserEntity(CreateUserDto createUserDto);

    UserResponse userEntityToUserResponse(User user);

    void upsertUser(UpsertUserDto upsertUserDto, @MappingTarget User user);
}
