package com.example.conversation.api.controller;

import com.example.conversation.api.dto.ConversationListResponseFCA1;
import com.example.conversation.api.enums.SortByFCA1;
import com.example.conversation.api.security.SecurityUtilFCA1;
import com.example.conversation.api.service.ConversationServiceFCA1;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for handling conversation-related API endpoints.
 */
@RestController
@RequestMapping("/conversations")
@Validated // Enable validation for method parameters
public class ConversationControllerFCA1 {

    private final ConversationServiceFCA1 conversationService;
    private final SecurityUtilFCA1 securityUtil;

    public ConversationControllerFCA1(ConversationServiceFCA1 conversationService, SecurityUtilFCA1 securityUtil) {
        this.conversationService = conversationService;
        this.securityUtil = securityUtil;
    }

    /**
     * Handles GET requests to fetch a paginated list of the authenticated user's conversations.
     *
     * @param searchQuery Optional search query to filter conversations.
     * @param sortBy      Sorting criteria for the conversation list.
     * @param page        The page number for pagination (1-based).
     * @param pageSize    The number of conversations per page.
     * @return A ResponseEntity containing the list of conversations and pagination info.
     */
    @GetMapping
    public ResponseEntity<ConversationListResponseFCA1> fetchConversations(
        @RequestParam(required = false)
        @Size(min = 0, max = 256, message = "searchQuery must be between 0 and 256 characters.") // min=0 for optional
        String searchQuery,

        @RequestParam(required = false, defaultValue = "recency")
        SortByFCA1 sortBy,

        @RequestParam(required = false, defaultValue = "1")
        @Min(value = 1, message = "page must be greater than or equal to 1.")
        Integer page,

        @RequestParam(required = false, defaultValue = "25")
        @Min(value = 1, message = "pageSize must be greater than or equal to 1.")
        @Max(value = 100, message = "pageSize must be less than or equal to 100.")
        Integer pageSize
    ) {
        UUID currentUserId = securityUtil.getCurrentUserId();
        ConversationListResponseFCA1 response = conversationService.fetchConversations(
            currentUserId, searchQuery, sortBy, page, pageSize
        );
        return ResponseEntity.ok(response);
    }
}
src/main/resources/application-conversation.properties
# Spring Boot application properties for Conversation API with FCA1 suffix.

# Disable Spring Boot banner
spring.main.banner-mode=off

# Configure Log4j2 as the logging implementation
logging.config=classpath:log4j2-conversation.xml

# Server port (optional, defaults to 8080)
server.port=8080

# Context path (optional)
server.servlet.context-path=/api/conversation
src/main/resources/log4j2-conversation.xml