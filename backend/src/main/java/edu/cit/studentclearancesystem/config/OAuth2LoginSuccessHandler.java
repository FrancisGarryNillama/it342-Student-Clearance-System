package edu.cit.studentclearancesystem.config;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        CustomUserPrincipal customPrincipal;

        if (principal instanceof CustomUserPrincipal cp) {
            customPrincipal = cp;
        } else if (principal instanceof OAuth2User oauthUser) {
            // Fallback wrapping
            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");
            String picture = oauthUser.getAttribute("picture");

            User user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setProfileUrl(picture);
            user.setRole(Role.STUDENT); // fallback or load from DB

            customPrincipal = new CustomUserPrincipal(user, oauthUser.getAttributes());

            // Inject into security context
            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                    customPrincipal,
                    customPrincipal.getAuthorities(),
                    ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } else {
            throw new RuntimeException("Unknown principal type: " + principal.getClass());
        }

        String role = customPrincipal.getUser().getRole().name();
        String redirectUrl = switch (role) {
            case "STUDENT" -> "http://localhost:3000/student";
            case "REGISTRAR" -> "http://localhost:3000/registrar";
            case "DEPARTMENT" -> "http://localhost:3000/department";
            default -> "http://localhost:3000";
        };

        response.sendRedirect(redirectUrl);
    }
}


