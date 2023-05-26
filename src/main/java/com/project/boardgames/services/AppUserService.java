package com.project.boardgames.services;

import com.project.boardgames.entities.Address;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.utilities.authentication.UserPassport;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    RequestResponse<UserPassport> login(HttpServletResponse response, String username, String password);

    RequestResponse<String> logout(HttpServletRequest request, HttpServletResponse response);

    AppUser registerUser(AppUser newUser, Address address);
}
