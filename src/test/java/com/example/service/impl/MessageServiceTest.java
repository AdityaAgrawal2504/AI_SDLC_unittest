package com.example.service.impl;

import com.example.dto.SendMessageDto;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IMessageRepository;
import com.example.repository.IUserRepository;
import com.example.service.IConversationService;
import com.example.service.IWebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock private IMessageRepository messageRepository;
    @Mock private IConversationService conversationService;
    @Mock private IUserRepository userRepository;
    @Mock private IWebSocketService webSocketService;
    @Spy private ObjectMapper objectMapper;

    @InjectMocks private MessageService messageService;

    private User sender;
    private User recipient;
    private SendMessageDto sendMessageDto;
    private UUID senderId = UUID.randomUUID();
    private UUID recipientId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(senderId);
        recipient = new User();
        recipient.setId(recipientId);

        sendMessageDto = new SendMessageDto();
        sendMessageDto.setRecipientId(recipientId);
        sendMessageDto.setContent("Hello World");
    }

    @Test
    void sendMessage_shouldSaveMessageAndPushToWebSocket() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.existsById(recipientId)).thenReturn(true);
        when(conversationService.findOrCreateConversation(senderId, recipientId)).thenReturn(conversation);
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> {
            Message msg = i.getArgument(0);
            msg.setId(UUID.randomUUID());
            return msg;
        });

        Message result = messageService.sendMessage(senderId, sendMessageDto);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Hello World");
        assertThat(result.getSender().getId()).isEqualTo(senderId);

        verify(messageRepository, times(1)).save(any(Message.class));
        verify(webSocketService, times(1)).sendToUser(eq(recipientId), anyString());
    }
}