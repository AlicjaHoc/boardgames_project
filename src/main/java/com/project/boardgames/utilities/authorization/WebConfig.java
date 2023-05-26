package com.project.boardgames.utilities.authorization;

import com.project.boardgames.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                .addPathPatterns("/api/v1/getAllOrders");
    }
}
