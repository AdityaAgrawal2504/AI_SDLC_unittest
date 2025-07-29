package com.example.service;

import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.*;
import com.example.service.queue.IQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock private IMessageRepository messageRepository;
    @Mock private IConversationRepository conversationRepository;
    @Mock private IUserRepository userRepository;
    @Mock private IParticipantRepository participantRepository;
    @Mock private IQueueService queueService;
    @Mock private com.example.service.logging.IEventLogger eventLogger;

    @InjectMocks private MessageService messageService;
    
    private User sender;
    private User recipient;

    @BeforeEach
    void setUp() {
        sender = User.builder().id(UUID.randomUUID()).build();
        recipient = User.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void sendMessage_whenConversationExists_shouldUseExistingConversation() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(conversationRepository.findConversationBetweenUsers(sender.getId(), recipient.getId())).thenReturn(Optional.of(new Conversation()));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArguments()[0]);

        assertNotNull(messageService.sendMessage(sender.getId(), recipient.getId(), "Hello"));
        verify(conversationRepository).findConversationBetweenUsers(sender.getId(), recipient.getId());
        verify(queueService).enqueueMessage(any(Message.class));
    }

    @Test
    void sendMessage_whenConversationDoesNotExist_shouldCreateNewConversation() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(conversationRepository.findConversationBetweenUsers(sender.getId(), recipient.getId())).thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class))).thenReturn(new Conversation());
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArguments()[0]);
        
        assertNotNull(messageService.sendMessage(sender.getId(), recipient.getId(), "Hello"));
        verify(conversationRepository).save(any(Conversation.class));
        verify(participantRepository).save(any());
        verify(queueService).enqueueMessage(any(Message.class));
    }
}