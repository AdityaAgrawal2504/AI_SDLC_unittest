package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    /**
     * The main method which serves as the entry point for the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        long endTime = System.currentTimeMillis();
        // This is a simple startup time log, more advanced logging is handled by Log4j2
        System.out.println("Application started in " + (endTime - startTime) + "ms");
    }
}