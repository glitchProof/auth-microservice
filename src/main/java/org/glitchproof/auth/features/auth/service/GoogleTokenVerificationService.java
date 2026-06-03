package org.glitchproof.auth.features.auth.service;

import org.glitchproof.auth.features.auth.dto.oauth.GoogleUserDto;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;

public interface GoogleTokenVerificationService {
    GoogleUserDto verify(TokenRequest tokenRequest);
}
