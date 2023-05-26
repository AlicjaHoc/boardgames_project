package com.project.boardgames.services;
import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.Address;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.entities.JwtToken;
import com.project.boardgames.entities.Role;
import com.project.boardgames.repositories.AddressRepository;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.repositories.JwtTokenRepository;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.utilities.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final PasswordHandler passwordEncoder;
    private final AppUserRepository userRepository;
    private final CustomAuthenticationProvider authenticationProvider;
    private final JwtTokenRepository jwtTokenRepository;
    @Autowired
    AddressRepository addressRepository;
    private final JwtTokenService jwtTokenService;

    public AppUserServiceImpl(AppUserRepository userRepository, PasswordHandler passwordEncoder, CustomAuthenticationProvider authenticationProvider, JwtTokenRepository jwtTokenRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenService = jwtTokenService;
    }
    @Override
    public Optional<AppUser> findUserByEmail(String email) {return userRepository.findByEmail(email);}
    @Override
    public RequestResponse<AppUser> createUser(AppUser user) {
        return null;
    }

    @Override
    public RequestResponse<AppUser> updateUser(AppUser user) {
        return null;
    }

    @Override
    public RequestResponse<String> deleteUser(Long userId) {
        return null;
    }

    @Override
    public RequestResponse<AppUser> getUserById(Long userId) {
        return null;
    }

    @Override
    public RequestResponse<AppUser> getUserByUsername(String username) {
        return null;
    }

    @Override
    public RequestResponse<List<AppUser>> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        if(users.isEmpty()) throw new AppException("There are no results", HttpStatus.NOT_FOUND.value(), "fail", true);
        return new RequestResponse<List<AppUser>>(true, users, "The registered users: ");
    }

    public RequestResponse<UserPassport> login(HttpServletResponse response, String email, String password) {
        // Authenticate the user
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            throw new AppException("Incorrect credentials.", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        // Retrieve the user from the repository
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND.value(), "fail", true));

        // Check if the account is blocked


        // Generate the JWT token and create the response
        String token = jwtTokenService.generateToken(user);
        UserPassport passport = new UserPassport(token, user.getEmail(), user.getFirstName(), user.getRole().name(), user.getId());
        CookieUtil.setAuthCookie(response, token);
        return new RequestResponse<UserPassport>(true, passport, "You were successfully logged in");
    }

    @Override
    public RequestResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
            String tokenCookie = CookieUtil.extractToken(request, "authCookie");
            if(tokenCookie == null) throw new AppException("You are not logged in", HttpStatus.BAD_REQUEST.value(), "fail", true);
            JwtToken jwt = jwtTokenRepository.findByToken(tokenCookie);
            jwtTokenRepository.delete(jwt);
            CookieUtil.clearCookie(response);
            return new RequestResponse<String>(true, "", "Logged out" );
        }

    @Override
    public AppUser registerUser(AppUser newUser, Address address) {
        String email = newUser.getEmail();
        newUser.setRole(Role.USER);
        // Check if user already exists with this email address
        if (userRepository.findByEmail(email).isPresent())
            throw new AppException("User under this email already exists.", HttpStatus.BAD_REQUEST.value(), "fail", true);
        LocalDateTime createdAt = LocalDateTime.now();
        newUser.setCreatedAt(createdAt);
        // Check if password and password confirmation match
        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            throw new AppException("Password and password confirmation do not match", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        // Check if password meets complexity requirements
        String password = newUser.getPassword();
        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            throw new AppException("Password must be at least 8 characters long and contain at least one number", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        newUser.setPassword(passwordEncoder.encrypt(newUser.getPassword()));
        newUser.setConfirmPassword("");
        try {
            AppUser registeredUser = userRepository.save(newUser);
            if(address != null) {
                addressRepository.save(address);
                registeredUser.setAddress(address);
            }
            return userRepository.save(registeredUser);
        }catch(Exception e){
            throw new AppException("Something went wrong during the process of registration:" + e.getMessage(), HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
    }
}
