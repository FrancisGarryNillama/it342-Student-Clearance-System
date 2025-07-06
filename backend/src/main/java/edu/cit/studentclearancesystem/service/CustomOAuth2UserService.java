package edu.cit.studentclearancesystem.service;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.repository.UserRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(request);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        String googleId = (String) attributes.get("sub");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = User.builder()
                    .googleId(googleId)
                    .email(email)
                    .fullName(name)
                    .profileUrl(picture)
                    .role(Role.STUDENT)
                    .build();
        } else {
            user.setFullName(name);
            user.setProfileUrl(picture);
        }

        userRepository.save(user);

        return new CustomUserPrincipal(user, attributes); // ✅ key
    }
}
