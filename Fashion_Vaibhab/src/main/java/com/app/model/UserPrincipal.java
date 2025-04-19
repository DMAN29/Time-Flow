package com.app.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    // ✅ Convert roles to GrantedAuthority with ROLE_ prefix
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = user.getRole().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Or encoded password
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can modify this logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement lock logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Change if using credential expiration
    }

    @Override
    public boolean isEnabled() {
        return true; // Update if you manage user status
    }

    // Optional getter if needed elsewhere
    public User getUser() {
        return user;
    }
}
