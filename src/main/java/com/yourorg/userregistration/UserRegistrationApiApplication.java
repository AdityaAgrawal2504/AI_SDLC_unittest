package com.yourorg.userregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserRegistrationApiApplication {

	/**
	 * The main entry point for the Spring Boot application.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationApiApplication.class, args);
	}
}
```
```java
// src/main/java/com/yourorg/userregistration/model/UserURAPI_1201.java