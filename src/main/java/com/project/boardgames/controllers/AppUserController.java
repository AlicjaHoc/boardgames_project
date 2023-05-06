package com.project.boardgames.controllers;


import com.project.boardgames.entities.AppUser;
import com.project.boardgames.services.AppUserService;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RequestMapping("/api/v1")
@RestController
public class AppUserController {


    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

//    @GetMapping("/users")
//    public List<AppUser> getUsers() {
//        return appUserServiceImpl.getAllUsers();
//    }

    @PostMapping("/register")
    public ResponseEntity<RequestResponse<AppUser>> registerNewUser(@Valid @RequestBody AppUser newUser, BindingResult bindingResult) {
        // Sanitize input data
        String sanitizedFirstName = sanitizeInput(newUser.getFirstName());
        String sanitizedLastName = sanitizeInput(newUser.getLastName());
        String sanitizedEmail = sanitizeInput(newUser.getEmail());

        // Update user object with sanitized data
        newUser.setFirstName(sanitizedFirstName);
        newUser.setLastName(sanitizedLastName);
        newUser.setEmail(sanitizedEmail);

        // Register user
        AppUser createdUser = appUserService.registerUser(newUser, bindingResult);
        RequestResponse<AppUser> response = new RequestResponse<AppUser>(true, createdUser, "User was successfully registered");
        return ResponseEntity.ok(response);
    }

    private String sanitizeInput(String input) {
        // Replace any characters that are not alphanumeric or space with an empty string
        return input.replaceAll("[^a-zA-Z0-9 ]", "");
    }



//    @DeleteMapping("/deleteAccount")
//    public ResponseEntity<RequestResponse<String>> deleteUserAccount()
}

