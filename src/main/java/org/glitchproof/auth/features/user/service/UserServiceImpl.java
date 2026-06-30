package org.glitchproof.auth.features.user.service;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.user.interfaces.OnCredentialsCreate;
import org.glitchproof.auth.features.user.interfaces.OnOAuthCreate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.glitchproof.auth.features.user.dto.CreateUserDto;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        var user = internal()
                .getUserByEmail(email);

        return UserResponse.from(user);
    }

    @Override
    @Validated({ Default.class, OnCredentialsCreate.class })
    public UserResponse createCredentialsUser(CreateUserDto createUserDto) {
        return create(createUserDto);
    }

    @Override
    @Validated({ Default.class, OnOAuthCreate.class })
    public UserResponse createOAuthUser(CreateUserDto createUserDto) {
        return create(createUserDto);
    }

    @Transactional
    private UserResponse create( CreateUserDto createUserDto) {
        if(userRepository.existsByUsernameOrEmail(createUserDto.getUsername(), createUserDto.getEmail())){
            throw new DomainException(UserException.USERNAME_AND_EMAIL_EXISTS);
        }

        User user = userMapper.createUserRequestToUserEntity(createUserDto);

        user.setLastLogin(LocalDateTime.now());

        if (StringUtils.hasText(createUserDto.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));
        }

        userRepository.save(user);

        log.info("User created: {}", user);

        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String email, UpsertUserDto upsertUser) {
        var user = internal()
                .getUserByEmail(email);

        userMapper.upsertUser(upsertUser, user);

        userRepository.save(user);

        log.info("User updated: {}", user);

        return UserResponse.from(user);
    }


    @Override
    @Transactional
    public void updateLastLogin(String email) {
        User user = internal()
                .getUserByEmail(email);

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    public InternalServiceImpl internal(){
        return new InternalServiceImpl();
    }

    public class InternalServiceImpl
            implements UserService.InternalService  {

        @Override
        @Transactional(readOnly = true)
        public User getUserByEmail(String email) {
            return userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new DomainException(UserException.EMAIL_NOT_FOUND));
        }

        @Override
        @Transactional(readOnly = true)
        public User getUserById(UUID userID) {
            return userRepository
                    .findById(userID)
                    .orElseThrow(() -> new DomainException(UserException.EMAIL_NOT_FOUND));
        }


    }
}
