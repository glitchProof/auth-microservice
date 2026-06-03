package org.glitchproof.auth.features.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.glitchproof.auth.core.dto.ApiExceptionResponse;
import org.glitchproof.auth.features.auth.dto.RegisterRequest;
import org.glitchproof.auth.features.token.dto.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.glitchproof.auth.features.auth.service.PasswordAuthService;
import org.glitchproof.auth.features.auth.dto.credentials.PasswordAuthRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class PasswordAuthController {
    private final PasswordAuthService passwordAuthService;

    @PostMapping("/login/with-credentials")
    @Operation(
            tags = "Authentication",
            summary = "Login with email and password"
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
            description = "Authenticated failed (Missing invalid credentials)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiExceptionResponse.class
                    )
            )
    )
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody PasswordAuthRequest credentials
    ) {
        var response = passwordAuthService.login(credentials);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/register/with-credentials")
    @Operation(
            tags = "Authentication",
            summary = "Register with credentials"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Authenticated failed (Missing invalid credentials)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiExceptionResponse.class
                    )
            )
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
    public ResponseEntity<TokenResponse> register(
          @Valid @RequestBody RegisterRequest registerRequest
    ) {
        var response = passwordAuthService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
