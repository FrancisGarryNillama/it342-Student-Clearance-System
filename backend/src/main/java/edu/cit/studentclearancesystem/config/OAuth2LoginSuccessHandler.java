package edu.cit.studentclearancesystem.config;

import edu.cit.studentclearancesystem.entity.Role;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.UserRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

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
            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");
            String picture = oauthUser.getAttribute("picture");

            User user = userRepository.findByEmail(email);

            if (user == null) {
                user = User.builder()
                        .email(email)
                        .fullName(name)
                        .profileUrl(picture)
                        .role(Role.STUDENT) // default role
                        .build();
            } else {
                user.setFullName(name);
                user.setProfileUrl(picture);
                if (user.getRole() == null) {
                    user.setRole(Role.STUDENT);
                }
            }

            userRepository.save(user);
            customPrincipal = new CustomUserPrincipal(user, oauthUser.getAttributes());

        } else {
            throw new RuntimeException("Unknown principal type: " + principal.getClass());
        }

        // ✅ ALWAYS reassign the Authentication so Spring Security uses the correct principal + authorities
        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                customPrincipal,
                customPrincipal.getAuthorities(),
                ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // ✅ Redirect based on role
        String role = customPrincipal.getUser().getRole().name();
        String redirectUrl = switch (role) {
            case "STUDENT" -> "http://localhost:3000/student";
            case "REGISTRAR" -> "http://localhost:3000/registrar";
            case "DEPT_HEAD" -> "http://localhost:3000/department";
            default -> "http://localhost:3000";
        };

        response.sendRedirect(redirectUrl);
    }
}




