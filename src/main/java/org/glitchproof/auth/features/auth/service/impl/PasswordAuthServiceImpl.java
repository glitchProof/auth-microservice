package org.glitchproof.auth.features.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glitchproof.auth.features.auth.mapper.AuthMapper;
import org.glitchproof.auth.features.user.enums.AuthProvider;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.glitchproof.auth.features.auth.dto.RegisterRequest;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.glitchproof.auth.features.token.service.JwtService;
import org.springframework.security.core.AuthenticationException;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.auth.service.PasswordAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.glitchproof.auth.features.auth.dto.credentials.PasswordAuthRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordAuthServiceImpl
        implements PasswordAuthService {

    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse login(PasswordAuthRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        }catch (AuthenticationException e) {
            throw new DomainException(AuthException.CREDENTIALS_NOT_VALID);
        }

        log.info("user sign in: {}", loginRequest.email());

        userService.updateLastLogin(loginRequest.email());

        return jwtService.generatePairToken(loginRequest.email());
    }

    @Override
    public TokenResponse register(RegisterRequest registerRequest) {
        if(userService.existsByEmail(registerRequest.getEmail())) {
            throw new DomainException(AuthException.EMAIL_ALREADY_TAKEN);
        }

        if(userService.existsByUsername(registerRequest.getUsername())) {
            throw new DomainException(AuthException.USERNAME_ALREADY_TAKEN);
        }

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            throw new DomainException(
                    AuthException.PASSWORD_CONFIRM_NOT_MATCH
            );
        }

        var newUser = authMapper
                .registerRequestToCreateUserRequest(registerRequest);

        newUser.setProvider(AuthProvider.PASSWORD);

        userService.createUser(newUser);

        log.info("new user {} registered", newUser);

        return jwtService.generatePairToken(registerRequest.getEmail());
    }
}
