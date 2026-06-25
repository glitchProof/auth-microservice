package org.glitchproof.auth.features.user.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.glitchproof.auth.features.user.dto.UserResponse;
import org.glitchproof.auth.features.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.glitchproof.auth.features.user.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @GetMapping("/me")
    @Operation(
            tags = "User Operation's",
            summary = "Get valid user from authenticated token"
    )
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var user = userDetails.user();
        var userResponse = userMapper
                .userEntityToUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

}
