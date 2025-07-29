package com.example.service;

import com.example.dto.request.SendMessageRequest;
import com.example.dto.response.PaginatedMessagesResponse;
import com.example.dto.response.SendMessageResponse;

import java.util.UUID;

/**
 * Service interface for sending and retrieving messages.
 */
public interface IMessageService {
    /**
     * Creates and sends a message from one user to another.
     * @param senderId The ID of the message sender.
     * @param recipientId The ID of the message recipient.
     * @param content The message text.
     * @return A response indicating the message was accepted.
     */
    SendMessageResponse sendMessage(UUID senderId, UUID recipientId, String content);

    /**
     * Finds all messages within a conversation, with pagination.
     * @param userId The ID of the user requesting the messages (for authorization).
     * @param conversationId The ID of the conversation.
     * @param page The page number (0-indexed).
     * @param pageSize The number of messages per page.
     * @return A paginated response of messages.
     */
    PaginatedMessagesResponse findMessagesByConversation(UUID userId, UUID conversationId, int page, int pageSize);
}