package com.tsofim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TsofimServiceApplication {

	public static void main(String[] args)
	{
		System.setProperty("webdriver.gecko.driver",
				"driver/geckodriver");
		SpringApplication.run(TsofimServiceApplication.class, args);
	}

}
