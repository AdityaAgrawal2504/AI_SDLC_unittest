package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.PaginatedMessagesResponse;
import com.example.messagingapp.dto.SendMessageRequest;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.MessageStatus;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import com.example.messagingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private MessageServiceImpl messageService;

    private User sender;
    private User recipient;
    private Message message;
    private UUID senderId;
    private UUID recipientId;

    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
        sender = User.builder().id(senderId).build();
        recipient = User.builder().id(recipientId).build();
        message = Message.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .recipient(recipient)
                .content("Test content")
                .status(MessageStatus.SENT)
                .build();
    }
    
    /**
     * Tests successful message sending.
     */
    @Test
    void sendMessage_success() {
        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello");
        when(userService.findById(senderId)).thenReturn(Optional.of(sender));
        when(userService.findById(recipientId)).thenReturn(Optional.of(recipient));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageResponse response = messageService.sendMessage(senderId, request);

        assertNotNull(response);
        assertEquals(senderId, response.getSenderId());
        assertEquals(recipientId, response.getRecipientId());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    /**
     * Tests listing messages for a user.
     */
    @Test
    void listMessages_success() {
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));
        when(messageRepository.findMessagesForUser(eq(senderId), any(), any(Pageable.class))).thenReturn(messagePage);

        PaginatedMessagesResponse response = messageService.listMessages(senderId, 1, 10, "createdAt", "desc", null);

        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getPagination().getCurrentPage());
    }

    /**
     * Tests successfully marking a message as read.
     */
    @Test
    void markMessageAsRead_success() {
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));
        
        assertDoesNotThrow(() -> messageService.markMessageAsRead(recipientId, message.getId()));

        assertEquals(MessageStatus.READ, message.getStatus());
        verify(messageRepository, times(1)).save(message);
    }
    
    /**
     * Tests that a user cannot mark a message as read if they are not the recipient.
     */
    @Test
    void markMessageAsRead_notRecipient_throwsForbidden() {
        UUID otherUserId = UUID.randomUUID();
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        ApiException exception = assertThrows(ApiException.class, () -> messageService.markMessageAsRead(otherUserId, message.getId()));
        
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("CANNOT_ACCESS_RESOURCE", exception.getErrorCode());
        verify(messageRepository, never()).save(any(Message.class));
    }
}