package com.tsofim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TsofimServiceApplication {

	public static void main(String[] args)
	{
		System.setProperty("webdriver.gecko.driver",
				"driver/geckodriver");
		SpringApplication.run(TsofimServiceApplication.class, args);
	}

}
