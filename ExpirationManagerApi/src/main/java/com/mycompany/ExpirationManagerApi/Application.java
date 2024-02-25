package com.mycompany.ExpirationManagerApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name="scheduler.enabled")
public class Application {

	public static void main(String[] args) {

		try
		{
			LogManager.getLogManager()
					.readConfiguration(new FileInputStream( "src/main/resources/logger.properties"));
		}
		catch (IOException e)
		{
			System.err.println("Failed to load log config");
			System.exit(-1);
		}

		SpringApplication.run(Application.class, args);
	}

}
