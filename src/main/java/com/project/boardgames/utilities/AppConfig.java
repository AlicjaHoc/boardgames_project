package com.project.boardgames.utilities;

import com.project.boardgames.entities.AppUser;
import com.project.boardgames.entities.GenericEntity;
import com.project.boardgames.entities.Producer;
import com.project.boardgames.entities.Product;
import com.project.boardgames.repositories.*;
import com.project.boardgames.services.AppUserServiceImpl;
import com.project.boardgames.services.GenericServiceImpl;
import com.project.boardgames.services.JwtTokenService;
import com.project.boardgames.utilities.authentication.CustomAuthenticationProvider;
import com.project.boardgames.utilities.authentication.PasswordHandler;
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
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JwtTokenRepository jwtTokenRepository;

    @Autowired
    ProducerRepository producerRepository;

    @Value("${custom.password.salt}")
    private String salt;
    @Bean
    public GenericServiceImpl<Product> productGenericService() {
        return new GenericServiceImpl<>(productRepository);
    }
    @Bean
    public GenericServiceImpl<AppUser> userGenericService() {
        return new GenericServiceImpl<>(appUserRepository);
    }
    @Bean
    public GenericServiceImpl<Producer> producerGenericService() {
        return new GenericServiceImpl<>(producerRepository);
    }
    @Bean
    public String salt() {
        return salt;
    };
    @Bean
    public PasswordHandler passwordHandler(String salt){
        return new PasswordHandler(salt);
    };

    @Bean
    public <T extends GenericEntity> GenericServiceImpl<T> genericService(GenericRepository<T> genericRepository) {
        return new GenericServiceImpl<>(genericRepository);
    }

    @Bean
    public CustomAuthenticationProvider authenticationProvider(PasswordHandler passwordHandler, AppUserRepository userRepository){
        return new CustomAuthenticationProvider(passwordHandler, userRepository);
    }

    @Bean
    public JwtTokenService jwtTokenService(){
        return new JwtTokenService();
    }
    @Bean
    public AppUserServiceImpl appUserService(AppUserRepository appUserRepository, PasswordHandler passwordHandler, CustomAuthenticationProvider authenticationProvider, JwtTokenRepository jwtTokenRepository, JwtTokenService jwtTokenService){
        return new AppUserServiceImpl(appUserRepository, passwordHandler, authenticationProvider, jwtTokenRepository, jwtTokenService);
    }

    public SecurityConfig securityConfig(JwtTokenRepository jwtTokenRepository){
        return new SecurityConfig(jwtTokenRepository);
    }

}
