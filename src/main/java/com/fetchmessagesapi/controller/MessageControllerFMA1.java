package com.fetchmessagesapi.controller;

import com.fetchmessagesapi.constants.ApiConstantsFMA1;
import com.fetchmessagesapi.dto.MessageListResponseFMA1;
import com.fetchmessagesapi.service.MessageServiceFMA1;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Controller for handling the Fetch Messages API endpoint.
 */
@RestController
@RequestMapping(ApiConstantsFMA1.CONVERSATIONS_ENDPOINT)
@Validated
public class MessageControllerFMA1 {

    private final MessageServiceFMA1 messageService;

    public MessageControllerFMA1(MessageServiceFMA1 messageService) {
        this.messageService = messageService;
    }

    /**
     * GET /v1/conversations/{conversationId}/messages
     * Retrieves the message history for a specified conversation.
     * @param conversationId The unique identifier for the conversation.
     * @param limit The maximum number of messages to return.
     * @param cursor A pagination cursor from a previous request.
     * @param authentication The authenticated user principal.
     * @return A response entity containing the list of messages and pagination info.
     */
    @GetMapping(ApiConstantsFMA1.MESSAGES_ENDPOINT)
    public ResponseEntity<MessageListResponseFMA1> fetchMessages(
        @PathVariable UUID conversationId,
        @RequestParam(defaultValue = "" + ApiConstantsFMA1.DEFAULT_LIMIT)
        @Min(value = 1, message = "Parameter 'limit' must be at least 1.")
        @Max(value = ApiConstantsFMA1.MAX_LIMIT, message = "Parameter 'limit' must not exceed " + ApiConstantsFMA1.MAX_LIMIT + ".")
        int limit,
        @RequestParam(required = false) String cursor,
        Authentication authentication
    ) {
        UUID userId = (UUID) authentication.getPrincipal();
        MessageListResponseFMA1 response = messageService.fetchMessages(conversationId, limit, cursor, userId);
        return ResponseEntity.ok(response);
    }
}
```
```java
// FILENAME: src/test/java/com/fetchmessagesapi/service/MessageServiceImplTestFMA1.java