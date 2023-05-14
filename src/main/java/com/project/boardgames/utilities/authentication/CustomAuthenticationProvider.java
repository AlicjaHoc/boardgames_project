package com.project.boardgames.utilities.authentication;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.repositories.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordHandler passwordHandler;
    private AppUserRepository userRepository;

    public CustomAuthenticationProvider(PasswordHandler passwordHandler, AppUserRepository userRepository) {
        this.passwordHandler = passwordHandler;
        this.userRepository = userRepository;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println(email + " " + password);
        Optional<AppUser> user = userRepository.findByEmail(email);
        if(user.isEmpty()) throw new AppException("Provided credentials are wrong", HttpStatus.BAD_REQUEST.value(), "fail", true);
        String userPassword = user.map(AppUser::getPassword).orElse(null);
        String userRole = String.valueOf(user.map(AppUser::getRole).orElse(null));
        if (!passwordHandler.matches(password, userPassword)) {
            throw new AppException("Provided credentials are wrong", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole);
        List<String> credentials = new ArrayList<>(){{add(email); add(password);}};
        return new UsernamePasswordAuthenticationToken(user, credentials, List.of(authority));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
