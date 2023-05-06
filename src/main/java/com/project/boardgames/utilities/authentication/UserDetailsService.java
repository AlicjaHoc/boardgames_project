package com.project.boardgames.utilities.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserDetailsService {
    Collection<? extends GrantedAuthority> getAuthorities();


    String getPassword();

    String getUsername();

    UserDetailsService loadUserByUsername(String email);

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();
}
