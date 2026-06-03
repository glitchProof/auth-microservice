package org.glitchproof.auth.features.auth.service;

import org.glitchproof.auth.features.auth.dto.magic_link.MagicLinkRequest;
import org.glitchproof.auth.features.token.dto.TokenResponse;

public interface MagicLinkService {
    void send(MagicLinkRequest magicLinkRequest);
    TokenResponse validate(String tokenRequest);
}
