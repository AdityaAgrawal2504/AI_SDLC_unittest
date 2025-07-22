package com.example.conversation.history.fch01.controller;

import com.example.conversation.history.fch01.dto.PaginatedMessagesResponseFCHAPI01;
import com.example.conversation.history.fch01.logging.LoggableFCHAPI04;
import com.example.conversation.history.fch01.service.ConversationServiceFCHAPI01;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

/**
 * REST controller for handling conversation-related API endpoints.
 * mermaid
 * classDiagram
 *      ConversationControllerFCHAPI01 --|> ConversationServiceFCHAPI01 : delegates to
 *      ConversationControllerFCHAPI01 ..> PaginatedMessagesResponseFCHAPI01 : returns
 *      class ConversationControllerFCHAPI01{
 *          +fetchConversationHistory(conversationId, limit, before) ResponseEntity
 *      }
 */
@RestController
@RequestMapping("/conversations")
@Validated
@LoggableFCHAPI04
public class ConversationControllerFCHAPI01 {

    private final ConversationServiceFCHAPI01 conversationService;

    public ConversationControllerFCHAPI01(ConversationServiceFCHAPI01 conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * Retrieves a paginated history of messages for a single conversation.
     * Messages are returned in reverse chronological order (newest first).
     *
     * @param conversationId The unique identifier of the conversation.
     * @param limit The maximum number of messages to return (default: 20, max: 100).
     * @param before A cursor for pagination, fetching messages created strictly before this timestamp.
     * @return A paginated list of messages.
     */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<PaginatedMessagesResponseFCHAPI01> fetchConversationHistory(
            @PathVariable
            @UUID(message = "conversationId must be a valid UUID.")
            String conversationId,

            @RequestParam(required = false, defaultValue = "20")
            @Min(value = 1, message = "Limit must be greater than 0.")
            @Max(value = 100, message = "Limit must be less than or equal to 100.")
            Integer limit,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime before) {

        PaginatedMessagesResponseFCHAPI01 response = conversationService.fetchConversationHistory(
                java.util.UUID.fromString(conversationId),
                limit,
                before
        );

        return ResponseEntity.ok(response);
    }
}
src/test/java/com/example/conversation/history/fch01/service/ConversationServiceImplFCHAPI02Test.java