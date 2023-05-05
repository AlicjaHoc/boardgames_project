package com.project.boardgames.services;

import com.project.boardgames.entities.AppUser;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface AppUserService {

    Optional<AppUser> findUserByEmail(String email);

    RequestResponse<AppUser> createUser(AppUser user);

    RequestResponse<AppUser>  updateUser(AppUser user);

    RequestResponse<String>  deleteUser(Long userId);

    RequestResponse<AppUser>  getUserById(Long userId);

    RequestResponse<AppUser>  getUserByUsername(String username);

    RequestResponse<List<AppUser>> getAllUsers();

    RequestResponse<AppUser>login(String username, String password);

    RequestResponse<AppUser> logout();

    AppUser registerUser(@Valid @RequestBody AppUser newUser, BindingResult bindingResult);
}
