package com.example.controller;

import com.example.dto.PaginatedConversationsResponse;
import com.example.dto.PaginatedMessagesResponse;
import com.example.service.IConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Validated
public class ConversationController {

    private final IConversationService conversationService;
    
    /**
     * Handles GET /conversations
     * @param userId The authenticated user's ID.
     * @param page The page number.
     * @param limit The number of items per page.
     * @return A paginated list of conversations.
     */
    @Operation(summary = "Retrieves a paginated list of the user's conversations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<PaginatedConversationsResponse> listConversations(
            @RequestAttribute("userId") UUID userId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
        
        // Note: Ignoring sortBy and search for this implementation to keep it concise.
        // Sorting by 'updated_at' is a common default.
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("updatedAt").descending());
        PaginatedConversationsResponse response = conversationService.listUserConversations(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles GET /conversations/{conversation_id}/messages
     * @param userId The authenticated user's ID.
     * @param conversationId The ID of the conversation.
     * @param page The page number.
     * @param limit The number of items per page.
     * @return A paginated list of messages.
     */
    @Operation(summary = "Retrieves a paginated list of messages for a specific conversation.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{conversation_id}/messages")
    public ResponseEntity<PaginatedMessagesResponse> getConversationMessages(
            @RequestAttribute("userId") UUID userId,
            @PathVariable("conversation_id") UUID conversationId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit) {
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        PaginatedMessagesResponse response = conversationService.getConversationMessages(userId, conversationId, pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Handles PUT /conversations/{conversation_id}/read-status
     * @param userId The authenticated user's ID.
     * @param conversationId The ID of the conversation to mark as read.
     * @return No content.
     */
    @Operation(summary = "Updates the read status of a conversation.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{conversation_id}/read-status")
    public ResponseEntity<Void> updateReadStatus(
            @RequestAttribute("userId") UUID userId,
            @PathVariable("conversation_id") UUID conversationId) {
        
        conversationService.updateReadStatus(userId, conversationId);
        return ResponseEntity.noContent().build();
    }
}