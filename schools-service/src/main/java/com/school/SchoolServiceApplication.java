package com.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolServiceApplication {

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver",
				"driver/geckodriver");

		SpringApplication.run(SchoolServiceApplication.class, args);
	}

}
