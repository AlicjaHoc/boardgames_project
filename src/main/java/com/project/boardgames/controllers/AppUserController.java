package com.project.boardgames.controllers;


import com.project.boardgames.entities.AppUser;
import com.project.boardgames.services.AppUserService;
import com.project.boardgames.utilities.LoginRequestDTO;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.utilities.authentication.UserPassport;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users")
    public ResponseEntity<RequestResponse<List<AppUser>>> getAllUsers(){
        return ResponseEntity.ok(appUserService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<RequestResponse<AppUser>> registerNewUser(@Valid @RequestBody AppUser newUser, BindingResult bindingResult) {
        // Sanitize input data
        String sanitizedFirstName = sanitizeInput(newUser.getFirstName());
        String sanitizedLastName = sanitizeInput(newUser.getLastName());

        // Update user object with sanitized data
        newUser.setFirstName(sanitizedFirstName);
        newUser.setLastName(sanitizedLastName);

        // Register user
        AppUser createdUser = appUserService.registerUser(newUser, bindingResult);
        RequestResponse<AppUser> response = new RequestResponse<AppUser>(true, createdUser, "User was successfully registered");
        return ResponseEntity.ok(response);
    }

    private String sanitizeInput(String input) {
        // Replace any characters that are not alphanumeric or space with an empty string
        return input.replaceAll("[^a-zA-Z0-9 ]", "");
    }

    @PostMapping("/loginUser")

    public ResponseEntity<RequestResponse<UserPassport>> login(@Valid @RequestBody LoginRequestDTO loginRequest, BindingResult bindingResult, HttpServletResponse res) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        RequestResponse<UserPassport> response = appUserService.login(res, email, password);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<RequestResponse<AppUser>> deleteAccount() {
        return null;
    }

    @GetMapping("/logout")
    public ResponseEntity<RequestResponse<String>> logout() {
        return null;
    }

}

