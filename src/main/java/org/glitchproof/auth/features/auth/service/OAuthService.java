package org.glitchproof.auth.features.auth.service;

import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.token.dto.TokenResponse;

public interface OAuthService {
    TokenResponse loginOrRegisterWithGoogle(TokenRequest tokenRequest);
}
