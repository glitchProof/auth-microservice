package org.glitchproof.auth.features.preferences.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.dto.PreferenceUpdateRequest;
import org.glitchproof.auth.features.preferences.services.PreferenceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/auth/users/preferences")
@RequiredArgsConstructor
public class PreferenceController {
    private final PreferenceService preferenceService;

    @GetMapping
    @Operation(
            tags = "User Operation's",
            summary = "Get authenticated user preference's"
    )
    public ResponseEntity<PreferenceResponse> getPreference(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        var preferenceResponse = preferenceService
                .getPreference(userDetails.getUsername());

        return ResponseEntity
                .ok(preferenceResponse);
    }

    @PutMapping
    @Operation(
            tags = "User Operation's",
            summary = "Update authenticated user preference's"
    )
    public ResponseEntity<PreferenceResponse> updatePreference(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PreferenceUpdateRequest updatePreference
    ){
        var updatedPreferenceResponse = preferenceService
                .updatePreference(userDetails.getUsername(), updatePreference);

        return ResponseEntity
                .ok(updatedPreferenceResponse);
    }
}
