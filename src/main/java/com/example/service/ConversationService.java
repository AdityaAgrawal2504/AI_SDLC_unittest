package com.example.service;

import com.example.dto.response.ConversationSummary;
import com.example.dto.response.PaginatedConversationsResponse;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IConversationRepository;
import com.example.repository.IParticipantRepository;
import com.example.service.exception.ForbiddenException;
import com.example.service.exception.ResourceNotFoundException;
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
 * Implements business logic for managing conversations.
 */
@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {

    private final IConversationRepository conversationRepository;
    private final IParticipantRepository participantRepository;

    /**
     * Retrieves a paginated list of conversation summaries for a user.
     * @param userId The ID of the user.
     * @param page The page number (0-indexed).
     * @param pageSize The number of conversations per page.
     * @return A paginated response of conversation summaries.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedConversationsResponse findUserConversations(UUID userId, int page, int pageSize) {
        Page<Conversation> conversationPage = conversationRepository.findByUserId(userId, PageRequest.of(page, pageSize));

        List<ConversationSummary> summaries = conversationPage.getContent().stream()
                .map(conversation -> {
                    User otherParticipant = conversation.getParticipants().stream()
                            .map(p -> p.getUser())
                            .filter(u -> !u.getId().equals(userId))
                            .findFirst()
                            .orElse(null); // This case could be a group chat or self-chat

                    Message lastMessage = conversation.getMessages().isEmpty() ? null :
                        conversation.getMessages().get(conversation.getMessages().size() - 1);

                    return DtoMapper.toConversationSummary(conversation, otherParticipant, lastMessage, 0); // Unread count logic to be added
                })
                .collect(Collectors.toList());

        return PaginatedConversationsResponse.builder()
                .items(summaries)
                .page(conversationPage.getNumber() + 1)
                .pageSize(conversationPage.getSize())
                .totalItems(conversationPage.getTotalElements())
                .totalPages(conversationPage.getTotalPages())
                .build();
    }

    /**
     * Gets a specific conversation by its ID, ensuring the requesting user is a participant.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user requesting access.
     * @return The Conversation entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Conversation getConversationByIdForUser(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with ID: " + conversationId));

        boolean isParticipant = participantRepository.isUserParticipantInConversation(userId, conversationId);
        if (!isParticipant) {
            throw new ForbiddenException("User is not a participant of this conversation.");
        }
        return conversation;
    }
}