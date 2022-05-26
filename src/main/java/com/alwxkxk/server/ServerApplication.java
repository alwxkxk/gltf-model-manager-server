package com.alwxkxk.server;

import com.alwxkxk.server.entity.StorageProperties;
import com.alwxkxk.server.service.StorageService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Autowired
	private Environment env;


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		// 添加跨域请求的配置
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(env.getProperty("client.url"));
			}
		};
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();

			Flyway flyway = Flyway.configure().dataSource(
					env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"),
					env.getProperty("spring.datasource.password")
			).load();
			flyway.migrate();
		};
	}

}
