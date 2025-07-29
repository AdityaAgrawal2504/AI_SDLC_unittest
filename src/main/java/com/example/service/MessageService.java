package com.example.service;

import com.example.dto.response.PaginatedMessagesResponse;
import com.example.dto.response.SendMessageResponse;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.logging.IEventLogger;
import com.example.service.queue.IQueueService;
import com.example.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implements business logic for sending and retrieving messages.
 */
@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IConversationRepository conversationRepository;
    private final IParticipantRepository participantRepository;
    private final IUserRepository userRepository;
    private final IQueueService queueService;
    private final IEventLogger eventLogger;

    /**
     * Handles sending a message. Finds or creates a conversation, saves the message,
     * and queues it for real-time delivery.
     * @param senderId The ID of the message sender.
     * @param recipientId The ID of the message recipient.
     * @param content The message text.
     * @return A response indicating the message was accepted and queued.
     */
    @Override
    @Transactional
    public SendMessageResponse sendMessage(UUID senderId, UUID recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with ID: " + senderId));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with ID: " + recipientId));

        Conversation conversation = conversationRepository.findConversationBetweenUsers(senderId, recipientId)
                .orElseGet(() -> createConversation(sender, recipient));
        
        // Update conversation timestamp to bring it to the top of the list
        conversationRepository.save(conversation);

        Message message = Message.builder()
                .sender(sender)
                .conversation(conversation)
                .content(content)
                .status(MessageStatus.QUEUED)
                .build();

        Message savedMessage = messageRepository.save(message);

        // Enqueue message for asynchronous processing/delivery
        queueService.enqueueMessage(savedMessage);
        eventLogger.log("MessageQueued", "Message " + savedMessage.getId() + " queued for delivery.");

        return SendMessageResponse.builder()
                .messageId(savedMessage.getId())
                .status(savedMessage.getStatus())
                .build();
    }

    /**
     * Creates a new conversation between two users.
     * @param user1 The first user.
     * @param user2 The second user.
     * @return The newly created Conversation entity.
     */
    private Conversation createConversation(User user1, User user2) {
        Conversation newConversation = new Conversation();
        Conversation savedConversation = conversationRepository.save(newConversation);

        Participant p1 = new Participant(user1, savedConversation);
        Participant p2 = new Participant(user2, savedConversation);

        participantRepository.save(p1);
        participantRepository.save(p2);
        
        eventLogger.log("ConversationCreated", "Conversation created between " + user1.getId() + " and " + user2.getId());

        return savedConversation;
    }

    /**
     * Retrieves a paginated list of messages for a given conversation.
     * @param userId The ID of the user requesting the messages (for authorization).
     * @param conversationId The ID of the conversation.
     * @param page The page number (0-indexed).
     * @param pageSize The number of messages per page.
     * @return A paginated response of messages.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessagesResponse findMessagesByConversation(UUID userId, UUID conversationId, int page, int pageSize) {
        // Authorization: Check if user is part of the conversation
        if (!participantRepository.isUserParticipantInConversation(userId, conversationId)) {
             throw new com.example.service.exception.ForbiddenException("User is not authorized to view this conversation.");
        }

        Page<Message> messagePage = messageRepository.findByConversationIdOrderBySentAtDesc(conversationId, PageRequest.of(page, pageSize));

        List<com.example.dto.response.MessageDto> messageDtos = messagePage.getContent().stream()
                .map(DtoMapper::toMessageDto)
                .collect(Collectors.toList());

        return PaginatedMessagesResponse.builder()
                .items(messageDtos)
                .page(messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .totalItems(messagePage.getTotalElements())
                .totalPages(messagePage.getTotalPages())
                .build();
    }
}