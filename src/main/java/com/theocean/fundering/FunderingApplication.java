package com.theocean.fundering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class FunderingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunderingApplication.class, args);
	}

}
