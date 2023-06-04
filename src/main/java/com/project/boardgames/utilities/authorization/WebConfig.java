package com.project.boardgames.utilities.authorization;

import com.project.boardgames.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AppUserRepository userRepository;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // First interceptor for admin role
        AuthorizationInterceptor adminInterceptor = new AuthorizationInterceptor(userRepository, "ADMIN");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**"); // Add the path(s) that require admin authorization here

        // Second interceptor for user role
        AuthorizationInterceptor userInterceptor = new AuthorizationInterceptor(userRepository, "USER");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/api/v1/toCart") // Add the path(s) that require user authorization here
                .addPathPatterns("/api/v1/fromCart")
                .addPathPatterns("/api/v1/removeCart")
                .addPathPatterns("/api/v1/createOrder")
                .addPathPatterns("/api/v1/payment")
                .addPathPatterns("/api/v1/finalizeOrder")
                .addPathPatterns("/api/v1/getAllOrders")
                .addPathPatterns("/api/v1/cart");
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://127.0.0.1:3000") // Add the frontend's domain here
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }

}
