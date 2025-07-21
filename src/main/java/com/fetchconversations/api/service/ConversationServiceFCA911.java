package com.fetchconversations.api.service;

import com.fetchconversations.api.dto.*;
import com.fetchconversations.api.entity.ConversationFCA911;
import com.fetchconversations.api.entity.ConversationParticipantFCA911;
import com.fetchconversations.api.entity.UserFCA911;
import com.fetchconversations.api.enums.SortFieldFCA911;
import com.fetchconversations.api.enums.SortOrderFCA911;
import com.fetchconversations.api.logging.StructuredLoggerFCA911;
import com.fetchconversations.api.repository.ConversationRepositoryFCA911;
import com.fetchconversations.api.repository.spec.ConversationSpecificationFCA911;
import com.fetchconversations.api.util.AuthUtilFCA911;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles the business logic for fetching and managing conversations.
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceFCA911 {

    private final ConversationRepositoryFCA911 conversationRepository;
    private final AuthUtilFCA911 authUtil;
    private final StructuredLoggerFCA911 logger = new StructuredLoggerFCA911(ConversationServiceFCA911.class);

    /**
     * Retrieves a paginated list of conversations for the authenticated user, with filtering and sorting.
     *
     * @param query    Search term for filtering.
     * @param sortField  Field to sort by.
     * @param sortOrder  Order of sorting.
     * @param page     Page number (1-based).
     * @param pageSize Number of items per page.
     * @return A paginated response of conversation DTOs.
     */
    @Transactional(readOnly = true)
    public PaginatedConversationsResponseFCA911 getConversations(
            String query, SortFieldFCA911 sortField, SortOrderFCA911 sortOrder, int page, int pageSize) {

        return logger.logAround("getConversations", () -> {
            UUID currentUserId = authUtil.getCurrentUserId();

            Sort.Direction direction = sortOrder == SortOrderFCA911.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(direction, sortField.getDbField());
            Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

            Specification<ConversationFCA911> spec = Specification
                    .where(ConversationSpecificationFCA911.forUser(currentUserId))
                    .and(ConversationSpecificationFCA911.withSearchQuery(query));

            Page<ConversationFCA911> conversationPage = conversationRepository.findAll(spec, pageable);
            
            return buildPaginatedResponse(conversationPage, currentUserId);
        });
    }

    /**
     * Converts a Page of Conversation entities to the API response DTO.
     *
     * @param conversationPage The page object from the repository.
     * @param currentUserId The ID of the user requesting the data.
     * @return The fully constructed PaginatedConversationsResponse DTO.
     */
    private PaginatedConversationsResponseFCA911 buildPaginatedResponse(Page<ConversationFCA911> conversationPage, UUID currentUserId) {
        return PaginatedConversationsResponseFCA911.builder()
                .data(conversationPage.getContent().stream()
                        .map(convo -> convertToDto(convo, currentUserId))
                        .collect(Collectors.toList()))
                .pagination(PaginationInfoFCA911.builder()
                        .currentPage(conversationPage.getNumber() + 1)
                        .pageSize(conversationPage.getSize())
                        .totalPages(conversationPage.getTotalPages())
                        .totalItems(conversationPage.getTotalElements())
                        .build())
                .build();
    }

    /**
     * Converts a single Conversation entity to its DTO representation.
     *
     * @param conversation The entity to convert.
     * @param currentUserId The ID of the user for whom the DTO is being built.
     * @return The corresponding ConversationDto.
     */
    private ConversationDtoFCA911 convertToDto(ConversationFCA911 conversation, UUID currentUserId) {
        ConversationParticipantFCA911 currentUserParticipant = conversation.getParticipants().stream()
                .filter(p -> p.getUser().getId().equals(currentUserId))
                .findFirst()
                .orElse(null); // Should not happen if spec is correct

        return ConversationDtoFCA911.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .avatarUrl(conversation.getAvatarUrl())
                .participants(conversation.getParticipants().stream()
                        .map(p -> convertToParticipantDto(p.getUser()))
                        .collect(Collectors.toList()))
                .lastMessage(conversation.getLastMessage() != null ?
                        LastMessageDtoFCA911.builder()
                                .id(conversation.getLastMessage().getId())
                                .contentSnippet(truncate(conversation.getLastMessage().getContent(), 100))
                                .senderId(conversation.getLastMessage().getSender().getId())
                                .timestamp(conversation.getLastMessage().getTimestamp())
                                .isSeen(Objects.nonNull(currentUserParticipant) && currentUserParticipant.isLastMessageSeen())
                                .build()
                        : null)
                .unreadCount(Objects.nonNull(currentUserParticipant) ? currentUserParticipant.getUnreadCount() : 0)
                .isMuted(Objects.nonNull(currentUserParticipant) && currentUserParticipant.isMuted())
                .updatedAt(conversation.getUpdatedAt())
                .createdAt(conversation.getCreatedAt())
                .build();
    }
    
    private ParticipantDtoFCA911 convertToParticipantDto(UserFCA911 user) {
        return ParticipantDtoFCA911.builder()
                .userId(user.getId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
    
    private String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
```
```java
// src/main/java/com/fetchconversations/api/exception/GlobalExceptionHandlerFCA911.java