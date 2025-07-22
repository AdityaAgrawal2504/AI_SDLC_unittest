package com.example.service;

import com.example.dto.*;
import com.example.entity.ConversationEntity_CHAT_2017;
import com.example.entity.UserEntity_UATH_1016;
import com.example.enums.ConversationSortBy_CHAT_2004;
import com.example.exception.ConversationNotFoundException_CHAT_2010;
import com.example.exception.ForbiddenAccessException_CHAT_2011;
import com.example.repository.ConversationRepository_CHAT_2019;
import com.example.repository.MessageRepository_MSG_3011;
import com.example.repository.UserRepository_UATH_1017;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for handling business logic related to conversations.
 */
@Service
@RequiredArgsConstructor
public class ConversationService_CHAT_2005 {

    private final ConversationRepository_CHAT_2019 conversationRepository;
    private final MessageRepository_MSG_3011 messageRepository;
    private final UserRepository_UATH_1017 userRepository;

    /**
     * Retrieves a paginated list of conversations for a given user.
     */
    @Transactional(readOnly = true)
    public ListConversationsResponse_CHAT_2003 getUserConversations(
            UUID userId, int page, int limit,
            ConversationSortBy_CHAT_2004 sortBy, Sort.Direction sortOrder, String search) {

        Sort sort = buildSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<ConversationEntity_CHAT_2017> conversationPage = conversationRepository.findConversationsForUser(userId, search, pageable);

        List<ConversationSummaryDto_CHAT_2014> summaries = conversationPage.getContent().stream()
                .map(convo -> convertToSummaryDto(convo, userId))
                .collect(Collectors.toList());

        PaginationDto_CHAT_2013 pagination = new PaginationDto_CHAT_2013(
                conversationPage.getNumber() + 1,
                conversationPage.getTotalPages(),
                conversationPage.getTotalElements(),
                conversationPage.getSize()
        );

        return new ListConversationsResponse_CHAT_2003(summaries, pagination);
    }

    /**
     * Marks all messages in a conversation as read by the current user.
     */
    @Transactional
    public GenericSuccessResponse_UATH_1003 markConversationAsRead(UUID userId, UUID conversationId) {
        ConversationEntity_CHAT_2017 conversation = findAndVerifyConversationParticipant(conversationId, userId);
        messageRepository.markMessagesAsRead(conversationId, userId, Instant.now());
        return new GenericSuccessResponse_UATH_1003("Conversation marked as read.");
    }
    
    /**
     * Finds or creates a 1-on-1 conversation between two users.
     */
    @Transactional
    public ConversationEntity_CHAT_2017 findOrCreateConversation(UserEntity_UATH_1016 user1, UserEntity_UATH_1016 user2) {
        Set<UserEntity_UATH_1016> participants = Set.of(user1, user2);
        
        return conversationRepository.findByExactParticipants(participants)
            .orElseGet(() -> {
                ConversationEntity_CHAT_2017 newConversation = new ConversationEntity_CHAT_2017();
                newConversation.setParticipants(participants);
                return conversationRepository.save(newConversation);
            });
    }

    /**
     * Finds a conversation and verifies that the given user is a participant.
     */
    public ConversationEntity_CHAT_2017 findAndVerifyConversationParticipant(UUID conversationId, UUID userId) {
        ConversationEntity_CHAT_2017 conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException_CHAT_2010("The specified conversation does not exist."));

        boolean isParticipant = conversation.getParticipants().stream().anyMatch(p -> p.getId().equals(userId));
        if (!isParticipant) {
            throw new ForbiddenAccessException_CHAT_2011("The authenticated user is not a participant in this conversation.");
        }
        return conversation;
    }
    
    private Sort buildSort(ConversationSortBy_CHAT_2004 sortBy, Sort.Direction sortOrder) {
        if (sortBy == ConversationSortBy_CHAT_2004.latest_message_time) {
            return Sort.by(sortOrder, "lastMessage.timestamp");
        }
        // "unread_first" sorting needs more complex logic, potentially a custom query or sorting in memory.
        // For simplicity, we'll default to sorting by latest message time.
        return Sort.by(Sort.Direction.DESC, "lastMessage.timestamp");
    }

    private ConversationSummaryDto_CHAT_2014 convertToSummaryDto(ConversationEntity_CHAT_2017 convo, UUID currentUserId) {
        List<ParticipantDto_CHAT_2015> participants = convo.getParticipants().stream()
                .filter(p -> !p.getId().equals(currentUserId))
                .map(p -> new ParticipantDto_CHAT_2015(p.getId(), p.getDisplayName(), p.getAvatarUrl()))
                .toList();

        MessageSnippetDto_CHAT_2016 snippet = null;
        if (convo.getLastMessage() != null) {
            snippet = new MessageSnippetDto_CHAT_2016(
                convo.getLastMessage().getContent(),
                convo.getLastMessage().getTimestamp()
            );
        }

        long unreadCount = messageRepository.countUnreadMessages(convo.getId(), currentUserId);

        return new ConversationSummaryDto_CHAT_2014(
                convo.getId(),
                participants,
                snippet,
                unreadCount,
                false // Muted status not implemented yet
        );
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/MessageService_CHAT_2006.java
//