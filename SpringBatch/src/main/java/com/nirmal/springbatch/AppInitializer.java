package com.nirmal.springbatch;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * Spring Boot initialization class of the SpringBatch project
 * 
 * @author Nirmal Balasooriya
 * 
 * 
 */

@ComponentScan({ "com.nirmal.springbatch" })
@EnableJpaRepositories("com.nirmal.springbatch")
@SpringBootApplication(scanBasePackages = { "com.nirmal.springbatch" })
public class AppInitializer extends SpringBootServletInitializer {

	@Autowired
	DataSource dataSource;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppInitializer.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppInitializer.class, args);
	}

}