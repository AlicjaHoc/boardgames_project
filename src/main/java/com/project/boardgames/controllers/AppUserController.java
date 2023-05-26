package com.project.boardgames.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.project.boardgames.entities.Address;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.services.AppUserService;
import com.project.boardgames.utilities.LoginRequestDTO;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.utilities.authentication.UserPassport;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<RequestResponse<AppUser>> registerNewUser(@Valid @RequestBody JsonNode req, BindingResult bindingResult) {
        // Sanitize input data
        String sanitizedFirstName = sanitizeInput(req.get("firstName").asText());
        String sanitizedLastName = sanitizeInput(req.get("lastName").asText());
        String email = req.get("email").asText();
        String password = req.get("password").asText();
        String confirmPassword = req.get("confirmPassword").asText();
        AppUser newUser = new AppUser();
        newUser.setFirstName(sanitizedFirstName);
        newUser.setLastName(sanitizedLastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setConfirmPassword(confirmPassword);
        // Check if address information is provided
        Address address = null;
        if (req.has("address")) {
            JsonNode addressNode = req.get("address");
            address = new Address();
            address.setStreet(addressNode.get("street").asText());
            address.setCity(addressNode.get("city").asText());
            address.setState(addressNode.get("state").asText());
            address.setZip(addressNode.get("zip").asText());
            address.setCountry(addressNode.get("country").asText());
        }
        // Register user
        AppUser createdUser = appUserService.registerUser(newUser, address);
        RequestResponse<AppUser> response = new RequestResponse<AppUser>(true, createdUser, "User was successfully registered");
        return ResponseEntity.ok(response);
    }

    private String sanitizeInput(String input) {
        // Replace any characters that are not alphanumeric, space, Polish special letters, or forward slash with an empty string
        return input.replaceAll("[^a-zA-Z0-9 \\p{L}/]", "");
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
    public ResponseEntity<RequestResponse<String>> logout(HttpServletRequest request, HttpServletResponse res) {
        RequestResponse<String> response = appUserService.logout(request, res);
        return ResponseEntity.ok(response);
    }
}

