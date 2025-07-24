package com.example.service;

import com.example.dto.MessageResponse;
import com.example.dto.SendMessageRequest;
import com.example.exception.ResourceNotFoundException;
import com.example.model.*;
import com.example.repository.ConversationRepository;
import com.example.repository.MessageRepository;
import com.example.repository.ParticipantRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    /**
     * Sends a message from a sender to a recipient.
     * Finds an existing conversation or creates a new one if necessary.
     * @param senderId The ID of the user sending the message.
     * @param request DTO containing recipient ID and message content.
     * @return DTO representing the sent message.
     */
    @Override
    @Transactional
    public MessageResponse sendMessage(UUID senderId, SendMessageRequest request) {
        // Ensure recipient exists
        if (!userRepository.existsById(request.getRecipientId())) {
            throw new ResourceNotFoundException("Recipient user with ID " + request.getRecipientId() + " not found.");
        }
        
        // Ensure sender and recipient are not the same
        if(senderId.equals(request.getRecipientId())) {
            throw new IllegalArgumentException("Sender and recipient cannot be the same user.");
        }

        Conversation conversation = findOrCreateConversation(senderId, request.getRecipientId());

        Message message = Message.builder()
                .conversationId(conversation.getId())
                .senderId(senderId)
                .content(request.getContent())
                .build();

        Message savedMessage = messageRepository.save(message);

        // Update conversation's updated_at timestamp
        conversation.setUpdatedAt(savedMessage.getCreatedAt());
        conversationRepository.save(conversation);

        return mapMessageToResponse(savedMessage);
    }
    
    /**
     * Finds a direct conversation between two users or creates a new one.
     * @param user1Id ID of the first user.
     * @param user2Id ID of the second user.
     * @return The existing or newly created conversation.
     */
    private Conversation findOrCreateConversation(UUID user1Id, UUID user2Id) {
        return conversationRepository.findDirectConversationBetweenUsers(user1Id, user2Id)
                .orElseGet(() -> createConversation(user1Id, user22Id));
    }
    
    private Conversation createConversation(UUID user1Id, UUID user2Id) {
        Conversation newConversation = conversationRepository.save(new Conversation());
        
        Participant participant1 = Participant.builder()
            .userId(user1Id)
            .conversationId(newConversation.getId())
            .build();
            
        Participant participant2 = Participant.builder()
            .userId(user2Id)
            .conversationId(newConversation.getId())
            .build();
            
        participantRepository.save(participant1);
        participantRepository.save(participant2);
        
        return newConversation;
    }

    private MessageResponse mapMessageToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}