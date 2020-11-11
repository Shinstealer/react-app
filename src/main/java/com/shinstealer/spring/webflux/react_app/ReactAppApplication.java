package com.shinstealer.spring.webflux.react_app;


import com.shinstealer.spring.webflux.react_app.repository.ProfileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = ProfileRepository.class)
public class ReactAppApplication extends AbstractReactiveMongoConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(ReactAppApplication.class, args);
	}

    
    @Override
    protected String getDatabaseName() {
      
      return "test";
    }

}
