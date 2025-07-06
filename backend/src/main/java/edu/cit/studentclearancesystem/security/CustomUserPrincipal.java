package edu.cit.studentclearancesystem.security;

import edu.cit.studentclearancesystem.entity.User;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class CustomUserPrincipal implements OAuth2User, Authentication {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomUserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getFullName(); // or email
    }

    // Required by Authentication interface
    @Override public Object getPrincipal() { return this; }
    @Override public Object getCredentials() { return null; }
    @Override public Object getDetails() { return user; }
    @Override public boolean isAuthenticated() { return true; }
    @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
}

