package com.example.controller;

import com.example.dto.SendMessageRequest_MSG_3002;
import com.example.dto.SendMessageResponse_MSG_3003;
import com.example.service.MessageService_CHAT_2006;
import com.example.util.StructuredLogger_UTIL_9999;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller for sending messages via REST.
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController_MSG_3001 {

    private final MessageService_CHAT_2006 messageService;
    private final StructuredLogger_UTIL_9999 logger;
    
    /**
     * Sends a message to a recipient.
     */
    @PostMapping
    public ResponseEntity<SendMessageResponse_MSG_3003> sendMessage(
            Authentication authentication,
            @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody SendMessageRequest_MSG_3002 request) {
        
        return logger.logMethod("sendMessage", () -> {
            UUID senderId = UUID.fromString(authentication.getName());
            logger.info("User " + senderId + " sending message to " + request.recipientId());
            
            SendMessageResponse_MSG_3003 response = messageService.sendMessage(
                senderId,
                request.recipientId(),
                request.messageContent(),
                Optional.ofNullable(idempotencyKey)
            );
            
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        });
    }
}
```
```java
//
// Filename: src/main/java/com/example/controller/GlobalExceptionHandler_EX_9001.java
//