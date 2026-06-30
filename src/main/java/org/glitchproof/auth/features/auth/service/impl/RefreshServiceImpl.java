package org.glitchproof.auth.features.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.glitchproof.auth.features.token.dto.AccessToken;
import org.glitchproof.auth.features.user.service.UserService;
import org.glitchproof.auth.features.auth.service.RefreshService;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshServiceImpl
        implements RefreshService {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    public AccessToken refresh(TokenRequest tokenRequest) {
        final var refreshToken = tokenRequest.token();

        var userID = jwtUtils
                .validateAndGetSubjectFromRefreshToken(refreshToken);

        var user = userService.internal()
                .getUserById(userID);

        final String accessToken = jwtUtils.generateAccessToken(user.getId());

        return new AccessToken(accessToken);
    }
}
