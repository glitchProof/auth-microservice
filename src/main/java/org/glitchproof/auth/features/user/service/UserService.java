package org.glitchproof.auth.features.user.service;

import org.glitchproof.auth.features.user.dto.CreateUserRequest;
import org.glitchproof.auth.features.user.dto.UserResponse;

public interface UserService {
    UserResponse getUserByEmail(String email);

    UserResponse createUser(CreateUserRequest createUserRequest);

    void updateLastLogin(String email);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameOrEmail(String username, String email);
}
