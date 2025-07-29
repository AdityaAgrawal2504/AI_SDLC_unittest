package com.example.controller;

import com.example.dto.request.SendMessageRequest;
import com.example.dto.response.PaginatedMessagesResponse;
import com.example.dto.response.SendMessageResponse;
import com.example.model.User;
import com.example.service.IMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for sending and retrieving chat messages.
 */
@RestController
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final IMessageService messageService;

    /**
     * Sends a message to another user.
     * @param user The authenticated user principal (sender).
     * @param request The message details, including recipient and content.
     * @return A response entity confirming the message was accepted.
     */
    @PostMapping("/messages")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SendMessageRequest request) {
        SendMessageResponse response = messageService.sendMessage(user.getId(), request.getRecipientId(), request.getContent());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /**
     * Retrieves messages for a specific conversation.
     * @param user The authenticated user principal.
     * @param conversationId The ID of the conversation.
     * @param page The page number to retrieve.
     * @param pageSize The number of items per page.
     * @return A paginated list of messages.
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<PaginatedMessagesResponse> getConversationMessages(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize) {
        PaginatedMessagesResponse response = messageService.findMessagesByConversation(user.getId(), conversationId, page - 1, pageSize);
        return ResponseEntity.ok(response);
    }
}