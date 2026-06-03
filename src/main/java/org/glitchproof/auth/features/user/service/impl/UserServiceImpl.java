package org.glitchproof.auth.features.user.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

@Slf4j
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
    public UserResponse createUser(@Valid CreateUserDto createUserDto) {
        if(userRepository.existsByUsernameOrEmail(createUserDto.getUsername(), createUserDto.getEmail())){
            throw new DomainException(UserException.USERNAME_AND_EMAIL_EXISTS);
        }

        User user = userMapper.createUserRequestToUserEntity(createUserDto);

        user.setLastLogin(LocalDateTime.now());
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));

        userRepository.save(user);

        return userMapper.userEntityToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String email, UpsertUserDto upsertUser) {
        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        userMapper.upsertUser(upsertUser, user);

        userRepository.save(user);

        return userMapper.userEntityToUserResponse(user);
    }


    @Override
    public void updateLastLogin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + email));

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);
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
    public Boolean existsByGoogleSubId(String googleSubId) {
        return userRepository.existsByGoogleSubId(googleSubId);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
