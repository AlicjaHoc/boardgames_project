package com.project.boardgames;

import com.project.boardgames.utilities.AppConfig;
import com.project.boardgames.utilities.FileUploadConfig;
import com.project.boardgames.utilities.authorization.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan("com.project.boardgames.*")
@EnableWebMvc
@Import({AppConfig.class, WebConfig.class,FileUploadConfig.class})
public class BoardgamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardgamesApplication.class, args);
	}

}
