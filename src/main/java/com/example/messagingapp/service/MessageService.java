package com.example.messagingapp.service;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.PaginatedMessagesResponse;
import com.example.messagingapp.dto.SendMessageRequest;
import java.util.UUID;

public interface MessageService {
    /**
     * Asynchronously sends a message from a sender to a recipient.
     * @param senderId The ID of the user sending the message.
     * @param request DTO containing recipient ID and message content.
     * @return The initial state of the message DTO.
     */
    MessageResponse sendMessage(UUID senderId, SendMessageRequest request);

    /**
     * Lists messages for a specific user with pagination and sorting.
     * @param userId The ID of the user whose messages are to be listed.
     * @param page The page number.
     * @param pageSize The number of messages per page.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort direction.
     * @param search A search term to filter message content.
     * @return A paginated response of message DTOs.
     */
    PaginatedMessagesResponse listMessages(UUID userId, int page, int pageSize, String sortBy, String sortOrder, String search);

    /**
     * Marks a specific message as read by the user.
     * @param userId The ID of the user marking the message.
     * @param messageId The ID of the message to be marked as read.
     */
    void markMessageAsRead(UUID userId, UUID messageId);
}