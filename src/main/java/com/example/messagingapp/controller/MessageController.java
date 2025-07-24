package com.example.messagingapp.controller;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.PaginatedMessagesResponse;
import com.example.messagingapp.dto.SendMessageRequest;
import com.example.messagingapp.logging.Loggable;
import com.example.messagingapp.security.AuthenticatedUser;
import com.example.messagingapp.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Sends a new message to a recipient.
     * @param sendMessageRequest The request body containing message details.
     * @param authenticatedUser The currently authenticated user.
     * @return The created message response.
     */
    @PostMapping
    @Loggable
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest sendMessageRequest,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        MessageResponse messageResponse = messageService.sendMessage(authenticatedUser.getId(), sendMessageRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageResponse);
    }

    /**
     * Retrieves a paginated list of messages for the authenticated user.
     * @param authenticatedUser The currently authenticated user.
     * @param page The page number.
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort order ('asc' or 'desc').
     * @param search The search query for message content.
     * @return A paginated list of messages.
     */
    @GetMapping
    @Loggable
    public ResponseEntity<PaginatedMessagesResponse> listMessages(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String search) {
        PaginatedMessagesResponse response = messageService.listMessages(authenticatedUser.getId(), page, pageSize, sortBy, sortOrder, search);
        return ResponseEntity.ok(response);
    }

    /**
     * Marks a specific message as read.
     * @param messageId The ID of the message to mark as read.
     * @param authenticatedUser The currently authenticated user.
     * @return A confirmation message.
     */
    @PostMapping("/{messageId}/read")
    @Loggable
    public ResponseEntity<Map<String, String>> markMessageRead(@PathVariable UUID messageId,
                                                               @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        messageService.markMessageAsRead(authenticatedUser.getId(), messageId);
        return ResponseEntity.ok(Map.of("message", "Message marked as read."));
    }
}