package com.example.service;

import com.example.model.Conversation;
import com.example.model.User;
import com.example.repository.IConversationRepository;
import com.example.repository.IParticipantRepository;
import com.example.service.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock private IConversationRepository conversationRepository;
    @Mock private IParticipantRepository participantRepository;

    @InjectMocks private ConversationService conversationService;

    @Test
    void getConversationByIdForUser_whenUserIsParticipant_shouldReturnConversation() {
        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Conversation conversation = new Conversation();
        
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(participantRepository.isUserParticipantInConversation(userId, conversationId)).thenReturn(true);
        
        assertNotNull(conversationService.getConversationByIdForUser(conversationId, userId));
    }

    @Test
    void getConversationByIdForUser_whenUserIsNotParticipant_shouldThrowForbiddenException() {
        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Conversation conversation = new Conversation();
        
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(participantRepository.isUserParticipantInConversation(userId, conversationId)).thenReturn(false);
        
        assertThrows(ForbiddenException.class, () -> conversationService.getConversationByIdForUser(conversationId, userId));
    }
}