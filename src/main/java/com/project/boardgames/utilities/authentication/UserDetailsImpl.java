package com.project.boardgames.utilities.authentication;

import com.project.boardgames.entities.AppUser;
import com.project.boardgames.repositories.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private AppUser user;

    private final AppUserRepository userRepository;

    public UserDetailsImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<AppUser> appUserOptional = userRepository.findByEmail(email);
        appUserOptional.ifPresent(appUser -> this.user = appUser);
        if (appUserOptional.isPresent()) {
            user = appUserOptional.get();
            return this;
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}