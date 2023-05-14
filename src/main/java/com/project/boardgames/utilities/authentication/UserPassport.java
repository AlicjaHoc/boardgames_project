package com.project.boardgames.utilities.authentication;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.entities.Role;
import com.project.boardgames.repositories.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserPassport {

    public UserPassport(String jwt, String username, String firstName, String role, Long id) {
        this.jwt = jwt;
        this.username = username;
        this.firstName = firstName;
        this.role = role;
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String jwt;
    private String username;
    private String firstName;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;



    private Collection<? extends GrantedAuthority> getAuthorities(AppUser user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return authorities;
    }
}