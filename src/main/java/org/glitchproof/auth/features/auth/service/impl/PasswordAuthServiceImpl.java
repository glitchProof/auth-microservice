package org.glitchproof.auth.features.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class PasswordAuthServiceImpl
        implements PasswordAuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;
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


        userService.updateLastLogin(loginRequest.email());

        String accessToken = jwtService.generateAccessToken(loginRequest.email());
        String refreshToken = jwtService.generateRefreshToken(loginRequest.email());


        return new TokenResponse(accessToken, refreshToken);
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

        userService.createUser(
                userMapper
                        .registerRequestToCreateUserRequest(registerRequest)
        );

        String accessToken = jwtService.generateAccessToken(registerRequest.getEmail());
        String refreshToken = jwtService.generateRefreshToken(registerRequest.getEmail());


        return new TokenResponse(accessToken, refreshToken);
    }
}
