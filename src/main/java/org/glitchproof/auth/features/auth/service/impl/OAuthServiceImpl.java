package org.glitchproof.auth.features.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.auth.exception.AuthException;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.user.dto.UpsertUserDto;
import org.glitchproof.auth.features.user.enums.AuthProvider;
import org.glitchproof.auth.features.auth.mapper.AuthMapper;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.glitchproof.auth.features.token.service.JwtService;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.service.OAuthService;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.auth.dto.oauth.GoogleUserDto;
import org.glitchproof.auth.features.auth.service.GoogleTokenVerificationService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl
        implements OAuthService {

    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final GoogleTokenVerificationService googleTokenVerificationService;

    @Override
    public TokenResponse loginOrRegisterWithGoogle(TokenRequest tokenRequest) {
        GoogleUserDto googleUser = googleTokenVerificationService.verify(tokenRequest);

        if(userService.existsByEmail(googleUser.email())){
            return login(googleUser);
        }

        return register(googleUser);
    }

    private TokenResponse login(GoogleUserDto googleUserDto) {
        UpsertUserDto upsertUserDto = UpsertUserDto.builder()
                .provider(AuthProvider.GOOGLE)
                .lastLogin(LocalDateTime.now())
                .googleSubId(googleUserDto.subId())
                .build();

        var updatedUser = userService.updateUser(googleUserDto.email(), upsertUserDto);

        log.info("user sing in with google {}", googleUserDto.email());

        return jwtUtils.generatePairToken(updatedUser.email());
    }

    private TokenResponse register(GoogleUserDto googleUserDto) {
        var newUser = authMapper.toCreateUserRequest(googleUserDto);

        newUser.setProvider(AuthProvider.GOOGLE);

        var createdUser = userService.createUser(newUser);

        log.info("new user {} registered with google", newUser);

        return jwtUtils.generatePairToken(createdUser.email());
    }
}
