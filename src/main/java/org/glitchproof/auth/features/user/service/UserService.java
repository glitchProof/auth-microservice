package org.glitchproof.auth.features.user.service;

import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.glitchproof.auth.features.user.dto.UserResponse;

public interface UserService {
    UserResponse getUserByEmail(String email);

    UserResponse createUser(CreateUserDto createUserDto);
    UserResponse updateUser(String email, UpsertUserDto upsertUser);

    void updateLastLogin(String email);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameOrEmail(String username, String email);
    Boolean existsByGoogleSubId(String googleSubId);
}
