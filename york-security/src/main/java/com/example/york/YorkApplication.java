package com.example.york;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(YorkApplication.class, args);
	}

}
