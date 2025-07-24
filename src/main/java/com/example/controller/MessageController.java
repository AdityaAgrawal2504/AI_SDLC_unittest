package com.example.controller;

import com.example.dto.MessageResponse;
import com.example.dto.SendMessageRequest;
import com.example.service.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;
    
    /**
     * Handles POST /messages
     * @param sendMessageRequest The request to send a message.
     * @param userId The authenticated user's ID, injected from the request attribute.
     * @return The created message.
     */
    @Operation(summary = "Sends a message to a specified recipient.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest sendMessageRequest,
            @RequestAttribute("userId") UUID userId) {
        MessageResponse messageResponse = messageService.sendMessage(userId, sendMessageRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
}