package com.yourorg.fetchconversationsapi.controller;

import com.yourorg.fetchconversationsapi.dto.PaginatedConversationsResponseFCA8123;
import com.yourorg.fetchconversationsapi.enums.ConversationSortFieldFCA8123;
import com.yourorg.fetchconversationsapi.enums.SortOrderFCA8123;
import com.yourorg.fetchconversationsapi.service.ConversationServiceFCA8123;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling conversation-related API endpoints.
 */
@RestController
@RequestMapping("/v1/conversations")
@RequiredArgsConstructor
@Validated
public class ConversationControllerFCA8123 {

    private final ConversationServiceFCA8123 conversationService;

    /**
     * Handles the GET request to fetch a paginated list of conversations.
     * @param query Optional search term.
     * @param sort Optional sort field.
     * @param order Optional sort order.
     * @param page Optional page number.
     * @param pageSize Optional page size.
     * @return A ResponseEntity containing the paginated list of conversations.
     */
    @GetMapping
    public ResponseEntity<PaginatedConversationsResponseFCA8123> fetchConversations(
        @RequestParam(required = false)
        @Size(max = 255, message = "Query length must not exceed 255 characters")
        String query,

        @RequestParam(required = false, defaultValue = "lastActivity")
        ConversationSortFieldFCA8123 sort,

        @RequestParam(required = false, defaultValue = "desc")
        SortOrderFCA8123 order,

        @RequestParam(required = false, defaultValue = "1")
        @Min(value = 1, message = "Page number must be at least 1")
        int page,

        @RequestParam(required = false, defaultValue = "25")
        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size must not exceed 100")
        int pageSize) {

        PaginatedConversationsResponseFCA8123 response = conversationService.fetchConversations(
            query, sort, order, page, pageSize
        );
        return ResponseEntity.ok(response);
    }
}
```
src/test/java/com/yourorg/fetchconversationsapi/YourAppApplicationTests.java
```java