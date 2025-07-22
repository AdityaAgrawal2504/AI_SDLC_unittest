package com.example.conversation.api.service;

import com.example.conversation.api.dto.ConversationListResponseFCA1;
import com.example.conversation.api.enums.SortByFCA1;

import java.util.UUID;

/**
 * Defines the business logic for managing conversations.
 */
public interface ConversationServiceFCA1 {

    /**
     * Fetches a paginated list of conversations for a given user.
     *
     * @param userId      The ID of the user whose conversations are to be fetched.
     * @param searchQuery An optional full-text search query.
     * @param sortBy      The sorting criteria for the results.
     * @param page        The page number to retrieve.
     * @param pageSize    The number of items per page.
     * @return A paginated list of conversations.
     */
    ConversationListResponseFCA1 fetchConversations(UUID userId, String searchQuery, SortByFCA1 sortBy, int page, int pageSize);
}
src/main/java/com.example/conversation/api/service/ConversationServiceImplFCA1.java