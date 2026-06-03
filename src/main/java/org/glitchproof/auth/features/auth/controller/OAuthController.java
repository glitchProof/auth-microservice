package org.glitchproof.auth.features.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import org.glitchproof.auth.core.dto.ApiExceptionResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.glitchproof.auth.features.auth.service.OAuthService;
import org.glitchproof.auth.features.auth.dto.oauth.TokenRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oauthService;

    @PostMapping("/login/with-oauth/google")
    @Operation(
            tags = "Authentication",
            summary = "Login with google o auth token"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = TokenResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiExceptionResponse.class
                    )
            )
    )
    public ResponseEntity<TokenResponse> loginWithGoogle(
            @Valid @RequestBody TokenRequest tokenRequest
    ){
        var response = oauthService.loginOrRegisterWithGoogle(tokenRequest);

        return ResponseEntity.ok(response);
    }
}
