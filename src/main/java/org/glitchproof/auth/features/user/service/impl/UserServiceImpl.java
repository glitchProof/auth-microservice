package org.glitchproof.auth.features.user.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.user.dto.CreateUserRequest;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(userMapper::userEntityToUserResponse)
                .orElseThrow(() -> new DomainException(UserException.EMAIL_NOT_FOUND));
    }

    @Override
    public UserResponse createUser(@Valid CreateUserRequest createUserRequest) {
        if(userRepository.existsByUsernameOrEmail(createUserRequest.getUsername(), createUserRequest.getEmail())){
            throw new DomainException(UserException.USERNAME_AND_EMAIL_EXISTS);
        }

        User user = userMapper.createUserRequestToUserEntity(createUserRequest);

        user.setLastLogin(LocalDateTime.now());
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(user);

        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .lastLogin(user.getLastLogin())
                .fullName(user.getFullName())
                .build();
    }


    @Override
    public void updateLastLogin(String email) {
        userRepository.findByEmail(email)
                .map((user) -> {

                    user.setLastLogin(LocalDateTime.now());

                    userRepository.save(user);

                    return Boolean.TRUE;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));
    }


    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
