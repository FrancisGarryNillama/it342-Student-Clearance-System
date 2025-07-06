package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/whoami")
    public String whoAmI(Authentication auth) {
        if (auth == null) return "❌ Not authenticated";

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserPrincipal cup) {
            User user = cup.getUser();
            return "✅ Authenticated: " + user.getEmail() + " | Role: " + user.getRole();
        }

        return "❌ Unexpected principal type: " + principal.getClass().getName();
    }
}
