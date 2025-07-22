package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * This single entry point initializes the Spring context for all APIs.
 */
@SpringBootApplication
public class Application {

    /**
     * The main method which serves as the entry point for the JVM.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
```java