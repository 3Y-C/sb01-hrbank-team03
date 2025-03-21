package com.sprint.part2.sb1hrbankteam03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sb1HrbankTeam03Application {

	public static void main(String[] args) {
		SpringApplication.run(Sb1HrbankTeam03Application.class, args);
	}

}
