src/main/java/com/example/controller/MessageController.java
package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedResultDto;
import com.example.dto.SendMessageDto;
import com.example.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

/**
 * Handles '/messages' and '/conversations/{id}/messages' for sending and retrieving messages.
 */
@RestController
public class MessageController {

    private final IMessageService messageService;

    @Autowired
    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Sends a message from the authenticated user to a specified recipient.
     * @param sendMessageDto DTO containing recipient phone number and message content.
     * @param authentication The current user's authentication object.
     * @return The created message DTO.
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @Valid @RequestBody SendMessageDto sendMessageDto,
            Authentication authentication) {
        String senderId = authentication.getName();
        MessageDto createdMessage = messageService.sendMessage(UUID.fromString(senderId), sendMessageDto);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of messages for a specific conversation.
     * @param conversationId The ID of the conversation.
     * @param page Page number for pagination.
     * @param pageSize Number of items per page.
     * @param search Search term to filter messages.
     * @param authentication The current user's authentication object.
     * @return A paginated list of messages.
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<PaginatedResultDto<MessageDto>> getConversationMessages(
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize,
            @RequestParam(required = false) String search,
            Authentication authentication) {
        String userId = authentication.getName();
        PaginatedResultDto<MessageDto> messages = messageService.getConversationMessages(UUID.fromString(userId), conversationId, page, pageSize, search);
        return ResponseEntity.ok(messages);
    }
}