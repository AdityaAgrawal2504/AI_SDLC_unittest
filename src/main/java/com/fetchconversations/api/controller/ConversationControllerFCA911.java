package com.fetchconversations.api.controller;

import com.fetchconversations.api.dto.PaginatedConversationsResponseFCA911;
import com.fetchconversations.api.enums.SortFieldFCA911;
import com.fetchconversations.api.enums.SortOrderFCA911;
import com.fetchconversations.api.service.ConversationServiceFCA911;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * REST controller for conversation-related API endpoints.
 */
@RestController
@RequestMapping("/v1/conversations")
@RequiredArgsConstructor
@Validated
public class ConversationControllerFCA911 {

    private final ConversationServiceFCA911 conversationService;

    /**
     * Fetches a paginated list of conversations for the authenticated user.
     *
     * @param query     Optional search term to filter conversations.
     * @param sort      The field to sort by (e.g., 'lastActivity', 'seen').
     * @param order     The sort order ('asc' or 'desc').
     * @param page      The page number for pagination (1-based).
     * @param pageSize  The number of items per page.
     * @return A ResponseEntity containing the paginated list of conversations.
     */
    @GetMapping
    public ResponseEntity<PaginatedConversationsResponseFCA911> fetchConversations(
            @RequestParam(required = false) @Size(max = 255) String query,
            @RequestParam(required = false, defaultValue = "lastActivity") SortFieldFCA911 sort,
            @RequestParam(required = false, defaultValue = "desc") SortOrderFCA911 order,
            @RequestParam(required = false, defaultValue = "1") @Min(1) Integer page,
            @RequestParam(required = false, defaultValue = "25") @Min(1) @Max(100) Integer pageSize
    ) {
        PaginatedConversationsResponseFCA911 response = conversationService.getConversations(query, sort, order, page, pageSize);
        return ResponseEntity.ok(response);
    }
}
```
```java
// src/test/java/com/fetchconversations/api/service/ConversationServiceFCA911Test.java