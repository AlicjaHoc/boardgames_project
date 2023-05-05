package com.project.boardgames.utilities;

import com.project.boardgames.controllers.AppUserController;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.services.AppUserService;
import com.project.boardgames.services.AppUserServiceImpl;
import com.project.boardgames.utilities.authentication.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@ComponentScan(basePackages = {"com.project.boardgames", "com.project.boardgames.controllers"})
public class AppConfig {

    @Autowired
    AppUserRepository appUserRepository;

    @Bean
    public AppUserServiceImpl appUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
        return new AppUserServiceImpl(appUserRepository, passwordEncoder);
    }
    @Bean
    public UserDetailsImpl userDetails(AppUserRepository appUserRepository) {
        return new UserDetailsImpl(appUserRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
