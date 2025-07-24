src/test/java/com/example/service/impl/ConversationService_Y1Z2ATest.java
package com.example.service.impl;

import com.example.dto.ConversationSummaryDto_V5W6X;
import com.example.exception.ForbiddenException_W3V4U;
import com.example.exception.ResourceNotFoundException_T5S6R;
import com.example.mapper.ConversationMapper_A5B6C;
import com.example.mapper.MessageMapper_X3Y4Z;
import com.example.model.Conversation_P3Q4R;
import com.example.model.Participant_S5T6U;
import com.example.model.User_M1N2O;
import com.example.repository.IConversationRepository_S5T4U;
import com.example.repository.IParticipantRepository_V3W2X;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationService_Y1Z2ATest {

    @Mock
    private IConversationRepository_S5T4U conversationRepository;
    @Mock
    private IParticipantRepository_V3W2X participantRepository;
    @Mock
    private ConversationMapper_A5B6C conversationMapper;
    @Mock
    private MessageMapper_X3Y4Z messageMapper;

    @InjectMocks
    private ConversationService_Y1Z2A conversationService;

    private User_M1N2O user;
    private Conversation_P3Q4R conversation;
    private ConversationSummaryDto_V5W6X conversationSummaryDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User_M1N2O();
        user.setId(UUID.randomUUID());

        conversation = new Conversation_P3Q4R();
        conversation.setId(UUID.randomUUID());
        conversation.setMessages(Collections.emptyList());

        conversationSummaryDto = new ConversationSummaryDto_V5W6X();
        conversationSummaryDto.setId(conversation.getId());
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void listConversations_Success() {
        Page<Conversation_P3Q4R> conversationPage = new PageImpl<>(Collections.singletonList(conversation));
        when(conversationRepository.findConversationsByUserId(user.getId(), pageable)).thenReturn(conversationPage);
        when(conversationMapper.toSummaryDto(any(), any())).thenReturn(conversationSummaryDto);
        when(participantRepository.countUnreadMessagesForUserInConversation(user.getId(), conversation.getId())).thenReturn(5L);

        Page<ConversationSummaryDto_V5W6X> result = conversationService.listConversations(user, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(5L, result.getContent().get(0).getUnreadCount());
        verify(conversationRepository, times(1)).findConversationsByUserId(user.getId(), pageable);
    }

    @Test
    void markAsRead_Success() {
        Participant_S5T6U participant = new Participant_S5T6U();
        participant.setLastReadAt(LocalDateTime.now().minusDays(1));

        when(conversationRepository.existsById(conversation.getId())).thenReturn(true);
        when(participantRepository.findByUserIdAndConversationId(user.getId(), conversation.getId())).thenReturn(Optional.of(participant));
        
        conversationService.markAsRead(user, conversation.getId());

        verify(participantRepository, times(1)).save(any(Participant_S5T6U.class));
        assertNotNull(participant.getLastReadAt());
        assertTrue(participant.getLastReadAt().isAfter(LocalDateTime.now().minusSeconds(5)));
    }

    @Test
    void markAsRead_ConversationNotFound() {
        when(conversationRepository.existsById(conversation.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException_T5S6R.class, () -> conversationService.markAsRead(user, conversation.getId()));
        verify(participantRepository, never()).findByUserIdAndConversationId(any(), any());
        verify(participantRepository, never()).save(any());
    }

    @Test
    void markAsRead_Forbidden() {
        when(conversationRepository.existsById(conversation.getId())).thenReturn(true);
        when(participantRepository.findByUserIdAndConversationId(user.getId(), conversation.getId())).thenReturn(Optional.empty());

        assertThrows(ForbiddenException_W3V4U.class, () -> conversationService.markAsRead(user, conversation.getId()));
        verify(participantRepository, never()).save(any());
    }
}