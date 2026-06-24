package org.glitchproof.auth.features.auth.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.glitchproof.auth.features.token.dto.AccessToken;
import org.glitchproof.auth.core.exception.DomainException;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.service.RefreshService;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.token.exception.JwtException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshServiceImpl
        implements RefreshService {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    @SneakyThrows
    public AccessToken refresh(TokenRequest tokenRequest) {
        final var refreshToken = tokenRequest.token();

        var subject = jwtUtils
                .validateAndGetSubjectFromRefreshToken(refreshToken);

        if(!userService.existsByEmail(subject)) {
            throw new DomainException(JwtException.INVALID);
        }

        final String accessToken = jwtUtils.generateAccessToken(subject);

        return new AccessToken(accessToken);
    }
}
