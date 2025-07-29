package com.example.service;

import com.example.dto.response.PaginatedConversationsResponse;
import com.example.model.Conversation;

import java.util.UUID;

/**
 * Service interface for managing conversations.
 */
public interface IConversationService {
    /**
     * Finds all conversations for a specific user, with pagination.
     * @param userId The ID of the user.
     * @param page The page number (0-indexed).
     * @param pageSize The number of conversations per page.
     * @return A paginated response of conversation summaries.
     */
    PaginatedConversationsResponse findUserConversations(UUID userId, int page, int pageSize);

    /**
     * Retrieves a specific conversation if the user is a participant.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user requesting access.
     * @return The Conversation entity.
     * @throws com.example.service.exception.ResourceNotFoundException if conversation not found.
     * @throws com.example.service.exception.ForbiddenException if user is not a participant.
     */
    Conversation getConversationByIdForUser(UUID conversationId, UUID userId);
}