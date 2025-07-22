package com.example.conversation.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Main Spring Boot application class for the Conversation API.
 * Disables DataSourceAutoConfiguration as no database is used in this mock setup.
 * Enables AspectJ auto-proxying for AOP (e.g., logging aspect).
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
public class ConversationApiApplicationFCA1 {

    public static void main(String[] args) {
        SpringApplication.run(ConversationApiApplicationFCA1.class, args);
    }
}
src/main/java/com/example/conversation/api/logging/StructuredLoggerFCA1.java