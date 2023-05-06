package com.project.boardgames.utilities;

import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.services.AppUserServiceImpl;
import com.project.boardgames.utilities.authentication.PasswordHandler;
import com.project.boardgames.utilities.authentication.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.project.boardgames", "com.project.boardgames.controllers"})
public class AppConfig {

    @Autowired
    AppUserRepository appUserRepository;
    @Value("${custom.password.salt}")
    private String salt;

    @Bean
    public String salt() {
        return salt;
    };
    @Bean
    public PasswordHandler passwordHandler(String salt){
        return new PasswordHandler(salt);
    };
    @Bean
    public AppUserServiceImpl appUserService(AppUserRepository appUserRepository, PasswordHandler passwordHandler){
        return new AppUserServiceImpl(appUserRepository, passwordHandler);
    }
    @Bean
    public UserDetailsServiceImpl userDetails(AppUserRepository appUserRepository) {
        return new UserDetailsServiceImpl(appUserRepository);
    }

}
