package com.mycompany.ExpirationManagerApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name="scheduler.enabled")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
