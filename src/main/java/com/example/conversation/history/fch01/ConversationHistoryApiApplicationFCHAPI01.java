package com.example.conversation.history.fch01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Main entry point for the Conversation History Spring Boot application.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class ConversationHistoryApiApplicationFCHAPI01 {

    public static void main(String[] args) {
        SpringApplication.run(ConversationHistoryApiApplicationFCHAPI01.class, args);
    }

}
src/main/java/com/example/conversation/history/fch01/dto/ApiErrorFCHAPI01.java