package com.gymnast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymnastServerApplication {


	public static void main(String[] args)
	{
//need for starting
		System.setProperty("webdriver.gecko.driver",
				"driver/geckodriver");
		SpringApplication.run(GymnastServerApplication.class, args);
	}

}
