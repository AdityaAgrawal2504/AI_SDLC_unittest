package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * The main entry point for the Spring Boot application.
 * All APIs run under this single application context.
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
            .properties("spring.config.name=application-UserRegistrationService_F4B8",
                        "logging.config=classpath:log4j2-UserRegistrationService_F4B8.xml")
            .run(args);
	}

}
```
```java
src/main/java/com/example/dto/UserRegistrationRequest_F4B8.java