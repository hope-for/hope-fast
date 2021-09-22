package com.hope;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlywayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlywayApplication.class, args);
		final Flyway flyway = Flyway.configure()
				.dataSource("jdbc:mysql://localhost:3306/hope-fast?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456")
				.load();
		flyway.migrate();
	}

}
