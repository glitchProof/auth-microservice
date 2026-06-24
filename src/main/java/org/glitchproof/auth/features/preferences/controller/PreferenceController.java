package org.glitchproof.auth.features.preferences.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.glitchproof.auth.features.preferences.dto.PreferenceResponse;
import org.glitchproof.auth.features.preferences.services.PreferenceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/auth/users/preferences")
@RequiredArgsConstructor
public class PreferenceController {
    private final PreferenceService preferenceService;

    @GetMapping
    public ResponseEntity<PreferenceResponse> getPreference(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        var preferenceResponse = preferenceService
                .getPreference(userDetails.getUsername());

        return ResponseEntity
                .ok(preferenceResponse);
    }
}
