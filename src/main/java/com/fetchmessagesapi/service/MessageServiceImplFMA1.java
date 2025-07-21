package com.fetchmessagesapi.service;

import com.fetchmessagesapi.dto.MessageListResponseFMA1;
import com.fetchmessagesapi.dto.PaginationInfoFMA1;
import com.fetchmessagesapi.entity.MessageFMA1;
import com.fetchmessagesapi.exception.PermissionDeniedExceptionFMA1;
import com.fetchmessagesapi.exception.ResourceNotFoundExceptionFMA1;
import com.fetchmessagesapi.repository.ConversationParticipantRepositoryFMA1;
import com.fetchmessagesapi.repository.ConversationRepositoryFMA1;
import com.fetchmessagesapi.repository.MessageRepositoryFMA1;
import com.fetchmessagesapi.util.CursorUtilFMA1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the MessageService interface.
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImplFMA1 implements MessageServiceFMA1 {

    private final MessageRepositoryFMA1 messageRepository;
    private final ConversationRepositoryFMA1 conversationRepository;
    private final ConversationParticipantRepositoryFMA1 participantRepository;
    private final CursorUtilFMA1 cursorUtil;
    private final MessageMapperFMA1 messageMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MessageListResponseFMA1 fetchMessages(UUID conversationId, int limit, String cursor, UUID userId) {
        validateAccess(conversationId, userId);

        Instant cursorTimestamp = (cursor != null) ? cursorUtil.decode(cursor) : null;
        PageRequest pageRequest = PageRequest.of(0, limit);

        Slice<MessageFMA1> messageSlice = (cursorTimestamp == null)
            ? messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageRequest)
            : messageRepository.findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(conversationId, cursorTimestamp, pageRequest);

        List<MessageFMA1> messages = messageSlice.getContent();
        String nextCursor = null;
        if (messageSlice.hasNext() && !messages.isEmpty()) {
            Instant lastMessageTimestamp = messages.get(messages.size() - 1).getCreatedAt();
            nextCursor = cursorUtil.encode(lastMessageTimestamp);
        }

        PaginationInfoFMA1 paginationInfo = new PaginationInfoFMA1(nextCursor, messageSlice.hasNext());
        List<com.fetchmessagesapi.dto.MessageDtoFMA1> messageDtos = messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());

        return new MessageListResponseFMA1(messageDtos, paginationInfo);
    }

    /**
     * Validates if the user has permission to access the conversation.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user.
     */
    private void validateAccess(UUID conversationId, UUID userId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new ResourceNotFoundExceptionFMA1("Conversation not found.");
        }
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new PermissionDeniedExceptionFMA1("You do not have permission to access this conversation.");
        }
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/security/JwtAuthenticationFilterFMA1.java