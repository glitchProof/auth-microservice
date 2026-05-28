package org.glitchproof.auth.features.user.mapper;

import org.glitchproof.auth.features.auth.dto.RegisterRequest;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.glitchproof.auth.features.user.dto.CreateUserRequest;
import org.glitchproof.auth.features.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User createUserRequestToUserEntity(CreateUserRequest createUserRequest);

    CreateUserRequest registerRequestToCreateUserRequest(RegisterRequest registerRequest);

    UserResponse userEntityToUserResponse(User user);
}
