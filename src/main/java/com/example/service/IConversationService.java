package com.example.service;

import com.example.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IConversationService {
    Page<Conversation> getConversationsForUser(UUID userId, Pageable pageable);
    Optional<Conversation> findById(UUID conversationId);
    boolean isUserParticipant(UUID conversationId, UUID userId);
    void markConversationAsRead(UUID userId, UUID conversationId, OffsetDateTime timestamp);
    Conversation findOrCreateConversation(UUID user1Id, UUID user2Id);
}