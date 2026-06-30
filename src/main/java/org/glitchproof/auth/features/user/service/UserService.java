package org.glitchproof.auth.features.user.service;

import jakarta.validation.Valid;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.entity.User;

import java.util.UUID;

public interface UserService {
    UserResponse getUserByEmail(String email);

    UserResponse createCredentialsUser(@Valid CreateUserDto createUserDto);
    UserResponse createOAuthUser(@Valid CreateUserDto createUserDto);

    UserResponse updateUser(String email, UpsertUserDto upsertUser);

    void updateLastLogin(String email);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    InternalService internal();

    interface InternalService {
        User getUserByEmail(String email);
        User getUserById(UUID userID);
    }
}
