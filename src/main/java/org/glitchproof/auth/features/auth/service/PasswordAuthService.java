package org.glitchproof.auth.features.auth.service;

import org.glitchproof.auth.features.auth.dto.RegisterRequest;
import org.glitchproof.auth.features.auth.dto.credentials.PasswordAuthRequest;
import org.glitchproof.auth.features.token.dto.TokenResponse;

public interface PasswordAuthService {
    TokenResponse login(PasswordAuthRequest loginRequest);
    TokenResponse register(RegisterRequest registerRequest);
}
