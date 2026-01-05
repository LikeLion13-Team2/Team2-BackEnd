package com.foco.foco_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FocoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FocoBackendApplication.class, args);
	}

}
