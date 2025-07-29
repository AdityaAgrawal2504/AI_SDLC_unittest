package com.example.service.impl;

import com.example.model.Conversation;
import com.example.model.User;
import com.example.repository.IConversationRepository;
import com.example.repository.IUserRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversationServiceTest {

    @Mock private IConversationRepository conversationRepository;
    @Mock private IUserRepository userRepository;

    @InjectMocks private ConversationService conversationService;

    private User user1;
    private User user2;
    private Conversation conversation;
    private UUID user1Id = UUID.randomUUID();
    private UUID user2Id = UUID.randomUUID();
    private UUID conversationId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(user1Id);
        user2 = new User();
        user2.setId(user2Id);
        conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setParticipants(Set.of(user1, user2));
    }

    @Test
    void getConversationsForUser_shouldReturnPageOfConversations() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conversation> conversationPage = new PageImpl<>(List.of(conversation), pageable, 1);
        when(conversationRepository.findByParticipantId(user1Id, pageable)).thenReturn(conversationPage);

        Page<Conversation> result = conversationService.getConversationsForUser(user1Id, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(conversationId);
        verify(conversationRepository).findByParticipantId(user1Id, pageable);
    }
    
    @Test
    void findOrCreateConversation_whenExists_shouldReturnExisting() {
        when(conversationRepository.findPrivateConversationBetweenUsers(user1Id, user2Id)).thenReturn(Optional.of(conversation));

        Conversation result = conversationService.findOrCreateConversation(user1Id, user2Id);
        
        assertThat(result.getId()).isEqualTo(conversationId);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void findOrCreateConversation_whenNotExists_shouldCreateAndReturnNew() {
        when(conversationRepository.findPrivateConversationBetweenUsers(user1Id, user2Id)).thenReturn(Optional.empty());
        when(userRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2Id)).thenReturn(Optional.of(user2));
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(i -> i.getArgument(0));

        Conversation result = conversationService.findOrCreateConversation(user1Id, user2Id);

        assertThat(result.getParticipants()).containsExactlyInAnyOrder(user1, user2);
        verify(conversationRepository).save(any(Conversation.class));
    }
    
    @Test
    void isUserParticipant_whenUserIsInConversation_shouldReturnTrue() {
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        
        boolean isParticipant = conversationService.isUserParticipant(conversationId, user1Id);
        
        assertThat(isParticipant).isTrue();
    }
    
    @Test
    void isUserParticipant_whenUserIsNotInConversation_shouldReturnFalse() {
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        
        boolean isParticipant = conversationService.isUserParticipant(conversationId, UUID.randomUUID());
        
        assertThat(isParticipant).isFalse();
    }
}