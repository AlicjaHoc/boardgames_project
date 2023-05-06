package com.project.boardgames.services;

import com.project.boardgames.entities.Role;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.utilities.authentication.PasswordHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final PasswordHandler passwordEncoder;
    private final AppUserRepository userRepository;

    public AppUserServiceImpl(AppUserRepository userRepository, PasswordHandler passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        return null;
    }


    @Override
    public RequestResponse<AppUser> login(String username, String password) {
        return null;
    }

    @Override
    public RequestResponse<AppUser> logout() {
        return null;
    }

    @Override
    public AppUser registerUser(@Valid @RequestBody AppUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            throw new AppException("Invalid input: " + String.join(", ", errorMessages), HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

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
        return userRepository.save(newUser);
    }

}
