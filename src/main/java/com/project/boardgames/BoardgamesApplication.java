package com.project.boardgames;

import com.project.boardgames.utilities.AppConfig;
import com.project.boardgames.utilities.FileUploadConfig;
import com.project.boardgames.utilities.authorization.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("com.project.boardgames.*")
@Import({AppConfig.class, WebConfig.class,FileUploadConfig.class})
public class BoardgamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardgamesApplication.class, args);
	}
//	@Configuration
//	@EnableConfigurationProperties({WebMvcProperties.class})
//	public class StaticResourceConfiguration implements WebMvcConfigurer {
//
//		private final String staticResourceLocation;
//
//		public StaticResourceConfiguration(WebMvcProperties webMvcProperties) {
//			this.staticResourceLocation = webMvcProperties.getStaticPathPattern();
//		}
//
//		@Override
//		public void addResourceHandlers(ResourceHandlerRegistry registry) {
//			registry.addResourceHandler("/images/**")
//					.addResourceLocations(staticResourceLocation);
//		}
//}
}