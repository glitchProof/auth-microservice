package org.glitchproof.auth.features.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.glitchproof.auth.features.auth.service.MagicLinkService;
import org.glitchproof.auth.features.auth.dto.magic_link.MagicLinkRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MagicController {
    private final MagicLinkService magicLinkService;

    @PostMapping("/login/magic-link/send")
    @Operation(
            tags= "Authentication",
            summary = "Send magic link to email"
    )
    public ResponseEntity<Void> sendMagicLink(
            @Valid @RequestBody MagicLinkRequest magicLinkRequest
    ) {
        magicLinkService.send(magicLinkRequest);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login/magic-link/verify")
    @Operation(
            tags= "Authentication",
            summary = "Verify magic link token from magic link"
    )
    public ResponseEntity<TokenResponse> verifyMagicLink(
            @RequestParam String token
    ) {
        TokenResponse response = magicLinkService.validate(token);

        return ResponseEntity.ok(response);
    }

}
