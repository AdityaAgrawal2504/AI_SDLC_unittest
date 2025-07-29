package com.example.controller;

import com.example.dto.response.PaginatedConversationsResponse;
import com.example.model.User;
import com.example.service.IConversationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for managing user conversations.
 */
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Validated
public class ConversationController {

    private final IConversationService conversationService;

    /**
     * Retrieves a paginated list of conversations for the authenticated user.
     * @param user The authenticated user principal.
     * @param page The page number to retrieve.
     * @param pageSize The number of items per page.
     * @return A paginated list of conversation summaries.
     */
    @GetMapping
    public ResponseEntity<PaginatedConversationsResponse> listConversations(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int pageSize) {

        PaginatedConversationsResponse response = conversationService.findUserConversations(user.getId(), page - 1, pageSize);
        return ResponseEntity.ok(response);
    }
}