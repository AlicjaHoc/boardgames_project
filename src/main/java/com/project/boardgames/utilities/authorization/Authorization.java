package com.project.boardgames.utilities.authorization;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Authorization {
    final
    AppUserRepository userRepository;

    public Authorization(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser hasPrivileges(String jwtToken, String role) {
        System.out.println(2);
        JwtTokenService jwt = new JwtTokenService();
        String username = jwt.extractUsername(jwtToken);
        Optional<AppUser> oUser = userRepository.findByEmail(username);
        if(oUser.isPresent()) {
            AppUser user = oUser.get();
            if(user.getRole().toString() == "ADMIN") {
                return user;
            }
            else if(user.getRole().toString() == role) {
                return user;
            }
            else {throw new AppException("You are not authorized to view this page", HttpStatus.UNAUTHORIZED.value(), "fail", true);
            }
        }
        else {
            throw new AppException("Could not find the user", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
    }
}
