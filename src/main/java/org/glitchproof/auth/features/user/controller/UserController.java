package org.glitchproof.auth.features.user.controller;

import lombok.RequiredArgsConstructor;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        var response = userService
                .getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
