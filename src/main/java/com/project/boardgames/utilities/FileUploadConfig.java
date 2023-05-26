package com.project.boardgames.utilities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.context.annotation.Bean;

@Configuration
public class FileUploadConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        // Set the maximum file size (optional)
        multipartResolver.setMaxUploadSize(10 * 1024 * 1024); // 10MB
        return multipartResolver;
    }
}
