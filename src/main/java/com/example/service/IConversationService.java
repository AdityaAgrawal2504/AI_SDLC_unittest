package com.example.service;

import com.example.dto.PaginatedConversationsResponse;
import com.example.dto.PaginatedMessagesResponse;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IConversationService {
    PaginatedConversationsResponse listUserConversations(UUID userId, Pageable pageable);
    PaginatedMessagesResponse getConversationMessages(UUID userId, UUID conversationId, Pageable pageable);
    void updateReadStatus(UUID userId, UUID conversationId);
}