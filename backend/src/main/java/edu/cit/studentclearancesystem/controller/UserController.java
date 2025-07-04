package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public UserResponse getCurrentUser(Authentication authentication) {
        User user = ((CustomUserPrincipal) authentication.getPrincipal()).getUser();

        return new UserResponse(
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getProfileUrl()
        );
    }

    @Data
    static class UserResponse {
        private final String email;
        private final String fullName;
        private final String role;
        private final String profileUrl;
    }
}