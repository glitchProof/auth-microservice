package org.glitchproof.auth.features.user.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.glitchproof.auth.features.user.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            tags = "User Operation's",
            summary = "Get valid user from authenticated token"
    )
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        var response = userService
                .getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

}
