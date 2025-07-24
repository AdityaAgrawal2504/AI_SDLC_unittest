src/main/java/com/example/controller/MessageController_J7K8L.java
package com.example.controller;

import com.example.dto.MessageDto_S3T4U;
import com.example.dto.SendMessageDto_P1Q2R;
import com.example.model.User_M1N2O;
import com.example.service.interfaces.IMessageService_B3C4D;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for sending messages.
 */
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController_J7K8L {

    private final IMessageService_B3C4D messageService;

    /**
     * Handles POST /messages to send a new message.
     * @param currentUser The authenticated sender.
     * @param sendMessageDto The DTO containing message details.
     * @return The created message DTO.
     */
    @PostMapping
    public ResponseEntity<MessageDto_S3T4U> sendMessage(
            @AuthenticationPrincipal User_M1N2O currentUser,
            @Valid @RequestBody SendMessageDto_P1Q2R sendMessageDto) {
        MessageDto_S3T4U sentMessage = messageService.sendMessage(currentUser, sendMessageDto);
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }
}