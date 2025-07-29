src/main/java/com/example/controller/ConversationController.java
package com.example.controller;

import com.example.dto.ConversationDto;
import com.example.dto.PaginatedResultDto;
import com.example.service.IConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

/**
 * Handles '/conversations/*' endpoints for listing conversations and marking them as read.
 */
@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final IConversationService conversationService;

    @Autowired
    public ConversationController(IConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * Retrieves a paginated list of the user's conversations.
     * @param page Page number for pagination.
     * @param pageSize Number of items per page.
     * @param sortBy Sort order for conversations.
     * @param status Filter conversations by read/unread status.
     * @param authentication The current user's authentication object.
     * @return A paginated list of conversations.
     */
    @GetMapping
    public ResponseEntity<PaginatedResultDto<ConversationDto>> listConversations(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int pageSize,
            @RequestParam(defaultValue = "recency") String sortBy,
            @RequestParam(defaultValue = "all") String status,
            Authentication authentication) {
        String userId = authentication.getName();
        PaginatedResultDto<ConversationDto> conversations = conversationService.listConversations(UUID.fromString(userId), page, pageSize, sortBy, status);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Marks all messages within a specified conversation as read for the authenticated user.
     * @param conversationId The ID of the conversation to mark as read.
     * @param authentication The current user's authentication object.
     * @return ResponseEntity with status 204 (No Content).
     */
    @PutMapping("/{conversationId}/read")
    public ResponseEntity<Void> markConversationAsRead(
            @PathVariable UUID conversationId,
            Authentication authentication) {
        String userId = authentication.getName();
        conversationService.markConversationAsRead(UUID.fromString(userId), conversationId);
        return ResponseEntity.noContent().build();
    }
}