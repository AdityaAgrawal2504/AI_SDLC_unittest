package com.example.service;

import com.example.dto.*;
import com.example.exception.PermissionDeniedException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.Participant;
import com.example.model.User;
import com.example.repository.ConversationRepository;
import com.example.repository.MessageRepository;
import com.example.repository.ParticipantRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    /**
     * Lists all conversations for a given user, with pagination.
     * @param userId The ID of the user whose conversations to fetch.
     * @param pageable Pagination information.
     * @return A paginated list of conversation DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedConversationsResponse listUserConversations(UUID userId, Pageable pageable) {
        Page<Conversation> conversationPage = conversationRepository.findByParticipantId(userId, pageable);

        List<ConversationResponse> conversationResponses = conversationPage.getContent().stream()
                .map(conversation -> mapToConversationResponse(conversation, userId))
                .collect(Collectors.toList());

        return new PaginatedConversationsResponse(
                conversationResponses,
                conversationPage.getTotalElements(),
                conversationPage.getNumber() + 1,
                conversationPage.getSize()
        );
    }

    /**
     * Retrieves messages for a specific conversation, with pagination.
     * @param userId The ID of the user requesting the messages (for authorization).
     * @param conversationId The ID of the conversation.
     * @param pageable Pagination information.
     * @return A paginated list of message DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessagesResponse getConversationMessages(UUID userId, UUID conversationId, Pageable pageable) {
        authorizeUserForConversation(userId, conversationId);
        Page<Message> messagePage = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);

        List<MessageResponse> messageResponses = messagePage.getContent().stream()
                .map(this::mapMessageToResponse)
                .collect(Collectors.toList());

        return new PaginatedMessagesResponse(
                messageResponses,
                messagePage.getTotalElements(),
                messagePage.getNumber() + 1,
                messagePage.getSize()
        );
    }
    
    /**
     * Updates the last read timestamp for a user in a specific conversation.
     * @param userId The ID of the user.
     * @param conversationId The ID of the conversation.
     */
    @Override
    @Transactional
    public void updateReadStatus(UUID userId, UUID conversationId) {
        Participant participant = participantRepository.findByUserIdAndConversationId(userId, conversationId)
            .orElseThrow(() -> new PermissionDeniedException("User is not a participant of this conversation."));
            
        participant.setLastReadAt(OffsetDateTime.now());
        participantRepository.save(participant);
    }

    private void authorizeUserForConversation(UUID userId, UUID conversationId) {
        if (!participantRepository.existsByUserIdAndConversationId(userId, conversationId)) {
            throw new PermissionDeniedException("User does not have access to this conversation.");
        }
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation, UUID currentUserId) {
        List<UserResponse> participants = conversation.getParticipants().stream()
                .map(Participant::getUser)
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());

        MessageResponse lastMessage = messageRepository.findTopByConversationIdOrderByCreatedAtDesc(conversation.getId())
                .map(this::mapMessageToResponse)
                .orElse(null);

        long unreadCount = participantRepository.findByUserIdAndConversationId(currentUserId, conversation.getId())
            .map(p -> messageRepository.countUnreadMessages(conversation.getId(), currentUserId, p.getLastReadAt()))
            .orElse(0L);

        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(participants)
                .lastMessage(lastMessage)
                .unreadCount(unreadCount)
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }
    
    private UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
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