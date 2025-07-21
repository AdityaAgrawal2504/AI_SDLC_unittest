package com.fetchmessagesapi.service;

import com.fetchmessagesapi.dto.MessageListResponseFMA1;
import com.fetchmessagesapi.entity.MessageFMA1;
import com.fetchmessagesapi.exception.PermissionDeniedExceptionFMA1;
import com.fetchmessagesapi.exception.ResourceNotFoundExceptionFMA1;
import com.fetchmessagesapi.repository.ConversationParticipantRepositoryFMA1;
import com.fetchmessagesapi.repository.ConversationRepositoryFMA1;
import com.fetchmessagesapi.repository.MessageRepositoryFMA1;
import com.fetchmessagesapi.util.CursorUtilFMA1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTestFMA1 {

    @Mock
    private MessageRepositoryFMA1 messageRepository;
    @Mock
    private ConversationRepositoryFMA1 conversationRepository;
    @Mock
    private ConversationParticipantRepositoryFMA1 participantRepository;
    @Mock
    private CursorUtilFMA1 cursorUtil;
    @Mock
    private MessageMapperFMA1 messageMapper;

    @InjectMocks
    private MessageServiceImplFMA1 messageService;

    private UUID conversationId;
    private UUID userId;
    private int limit;

    @BeforeEach
    void setUp() {
        conversationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        limit = 10;
    }

    @Test
    void fetchMessages_shouldThrowResourceNotFound_whenConversationDoesNotExist() {
        when(conversationRepository.existsById(conversationId)).thenReturn(false);

        assertThrows(ResourceNotFoundExceptionFMA1.class,
                () -> messageService.fetchMessages(conversationId, limit, null, userId));

        verify(conversationRepository).existsById(conversationId);
        verifyNoInteractions(participantRepository, messageRepository);
    }

    @Test
    void fetchMessages_shouldThrowPermissionDenied_whenUserIsNotParticipant() {
        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(participantRepository.existsByConversationIdAndUserId(conversationId, userId)).thenReturn(false);

        assertThrows(PermissionDeniedExceptionFMA1.class,
                () -> messageService.fetchMessages(conversationId, limit, null, userId));

        verify(conversationRepository).existsById(conversationId);
        verify(participantRepository).existsByConversationIdAndUserId(conversationId, userId);
        verifyNoInteractions(messageRepository);
    }

    @Test
    void fetchMessages_shouldReturnFirstPage_whenCursorIsNull() {
        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(participantRepository.existsByConversationIdAndUserId(conversationId, userId)).thenReturn(true);

        MessageFMA1 message = new MessageFMA1();
        message.setCreatedAt(Instant.now());
        Slice<MessageFMA1> messageSlice = new SliceImpl<>(List.of(message), PageRequest.of(0, limit), true);

        when(messageRepository.findByConversationIdOrderByCreatedAtDesc(eq(conversationId), any(PageRequest.class)))
                .thenReturn(messageSlice);
        when(cursorUtil.encode(any(Instant.class))).thenReturn("encodedCursor");
        when(messageMapper.toDto(any(MessageFMA1.class))).thenReturn(new com.fetchmessagesapi.dto.MessageDtoFMA1());

        MessageListResponseFMA1 response = messageService.fetchMessages(conversationId, limit, null, userId);

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertTrue(response.getPagination().isHasMore());
        assertEquals("encodedCursor", response.getPagination().getNextCursor());
        verify(messageRepository).findByConversationIdOrderByCreatedAtDesc(any(), any());
    }

    @Test
    void fetchMessages_shouldReturnNextPage_whenCursorIsValid() {
        String cursor = "validCursor";
        Instant decodedTimestamp = Instant.now().minusSeconds(3600);

        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(participantRepository.existsByConversationIdAndUserId(conversationId, userId)).thenReturn(true);
        when(cursorUtil.decode(cursor)).thenReturn(decodedTimestamp);

        Slice<MessageFMA1> messageSlice = new SliceImpl<>(Collections.emptyList(), PageRequest.of(0, limit), false);
        when(messageRepository.findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(eq(conversationId), eq(decodedTimestamp), any(PageRequest.class)))
                .thenReturn(messageSlice);

        MessageListResponseFMA1 response = messageService.fetchMessages(conversationId, limit, cursor, userId);

        assertNotNull(response);
        assertTrue(response.getData().isEmpty());
        assertFalse(response.getPagination().isHasMore());
        assertNull(response.getPagination().getNextCursor());
        verify(messageRepository).findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(any(), any(), any());
    }

    @Test
    void fetchMessages_shouldReturnLastPage_withNoNextCursor() {
        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(participantRepository.existsByConversationIdAndUserId(conversationId, userId)).thenReturn(true);

        MessageFMA1 message = new MessageFMA1();
        Slice<MessageFMA1> messageSlice = new SliceImpl<>(List.of(message), PageRequest.of(0, limit), false);

        when(messageRepository.findByConversationIdOrderByCreatedAtDesc(eq(conversationId), any(PageRequest.class)))
                .thenReturn(messageSlice);
        when(messageMapper.toDto(any(MessageFMA1.class))).thenReturn(new com.fetchmessagesapi.dto.MessageDtoFMA1());


        MessageListResponseFMA1 response = messageService.fetchMessages(conversationId, limit, null, userId);

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertFalse(response.getPagination().isHasMore());
        assertNull(response.getPagination().getNextCursor());
        verify(cursorUtil, never()).encode(any());
    }
}
```
```java
// FILENAME: src/test/java/com/fetchmessagesapi/controller/MessageControllerTestFMA1.java