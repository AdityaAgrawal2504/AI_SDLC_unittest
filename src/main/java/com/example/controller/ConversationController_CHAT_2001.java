package com.example.controller;

import com.example.dto.GenericSuccessResponse_UATH_1003;
import com.example.dto.GetMessagesResponse_CHAT_2002;
import com.example.dto.ListConversationsResponse_CHAT_2003;
import com.example.enums.ConversationSortBy_CHAT_2004;
import com.example.service.ConversationService_CHAT_2005;
import com.example.service.MessageService_CHAT_2006;
import com.example.util.StructuredLogger_UTIL_9999;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Controller for managing chat conversations and messages.
 */
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Validated
public class ConversationController_CHAT_2001 {

    private final ConversationService_CHAT_2005 conversationService;
    private final MessageService_CHAT_2006 messageService;
    private final StructuredLogger_UTIL_9999 logger;

    /**
     * Fetches a paginated list of the user's conversations.
     */
    @GetMapping
    public ResponseEntity<ListConversationsResponse_CHAT_2003> listConversations(
            Authentication authentication,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
            @RequestParam(defaultValue = "latest_message_time") ConversationSortBy_CHAT_2004 sortBy,
            @RequestParam(defaultValue = "desc") Sort.Direction sortOrder,
            @RequestParam(required = false) String search) {
        
        return logger.logMethod("listConversations", () -> {
            String userId = authentication.getName();
            logger.info("Fetching conversations for user: " + userId);
            ListConversationsResponse_CHAT_2003 response = conversationService.getUserConversations(UUID.fromString(userId), page, limit, sortBy, sortOrder, search);
            return ResponseEntity.ok(response);
        });
    }

    /**
     * Retrieves the paginated message history for a specific conversation.
     */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<GetMessagesResponse_CHAT_2002> getConversationMessages(
            Authentication authentication,
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit) {
        
        return logger.logMethod("getConversationMessages", () -> {
            String userId = authentication.getName();
            logger.info("Fetching messages for conversation " + conversationId + " for user " + userId);
            GetMessagesResponse_CHAT_2002 response = messageService.getMessagesForConversation(UUID.fromString(userId), conversationId, page, limit);
            return ResponseEntity.ok(response);
        });
    }

    /**
     * Marks all messages in a conversation as 'seen' for the current user.
     */
    @PostMapping("/{conversationId}/read")
    public ResponseEntity<GenericSuccessResponse_UATH_1003> markConversationAsRead(
            Authentication authentication,
            @PathVariable UUID conversationId) {
        
        return logger.logMethod("markConversationAsRead", () -> {
            String userId = authentication.getName();
            logger.info("Marking conversation " + conversationId + " as read for user " + userId);
            GenericSuccessResponse_UATH_1003 response = conversationService.markConversationAsRead(UUID.fromString(userId), conversationId);
            return ResponseEntity.ok(response);
        });
    }
}
```
```java
//
// Filename: src/main/java/com/example/controller/MessageController_MSG_3001.java
//