package org.glitchproof.auth.features.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.core.annotations.RateLimiter;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;
import org.glitchproof.auth.features.auth.service.RefreshService;
import org.glitchproof.auth.features.token.dto.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    @RateLimiter(capacity = 2)
    @Operation(
            tags = "Authentication",
            summary = "generate new access token form refresh token "
    )
    public ResponseEntity<AccessToken> refresh(
            @Valid @RequestBody TokenRequest tokenRequest
    ) {
        var accessToken = refreshService.refresh(tokenRequest);

        return ResponseEntity.ok(accessToken);
    }
}
