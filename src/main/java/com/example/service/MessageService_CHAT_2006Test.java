package com.example.service;

import com.example.dto.SendMessageResponse_MSG_3003;
import com.example.entity.ConversationEntity_CHAT_2017;
import com.example.entity.MessageEntity_MSG_3010;
import com.example.entity.UserEntity_UATH_1016;
import com.example.exception.UserNotFoundException_UATH_1014;
import com.example.repository.MessageRepository_MSG_3011;
import com.example.repository.UserRepository_UATH_1017;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageService_CHAT_2006Test {

    @Mock
    private MessageRepository_MSG_3011 messageRepository;

    @Mock
    private UserRepository_UATH_1017 userRepository;

    @Mock
    private ConversationService_CHAT_2005 conversationService;

    @InjectMocks
    private MessageService_CHAT_2006 messageService;

    @Test
    void sendMessage_whenRecipientNotFound_shouldThrowException() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UserEntity_UATH_1016 sender = new UserEntity_UATH_1016();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipientId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException_UATH_1014.class, () -> {
            messageService.sendMessage(senderId, recipientId, "Hello", Optional.empty());
        });
    }

    @Test
    void sendMessage_withValidData_shouldReturnResponse() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UserEntity_UATH_1016 sender = new UserEntity_UATH_1016();
        UserEntity_UATH_1016 recipient = new UserEntity_UATH_1016();
        ConversationEntity_CHAT_2017 conversation = new ConversationEntity_CHAT_2017();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        when(conversationService.findOrCreateConversation(sender, recipient)).thenReturn(conversation);
        when(messageRepository.save(any(MessageEntity_MSG_3010.class))).thenAnswer(i -> i.getArguments()[0]);

        SendMessageResponse_MSG_3003 response = messageService.sendMessage(senderId, recipientId, "Hello", Optional.empty());

        assertNotNull(response);
        assertEquals("QUEUED_FOR_DELIVERY", response.status());
    }

    @Test
    void sendMessage_withIdempotencyKey_whenMessageExists_shouldReturnExistingMessage() {
        String idempotencyKey = UUID.randomUUID().toString();
        MessageEntity_MSG_3010 existingMessage = new MessageEntity_MSG_3010();
        existingMessage.setId(UUID.randomUUID());

        when(messageRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(existingMessage));

        SendMessageResponse_MSG_3003 response = messageService.sendMessage(UUID.randomUUID(), UUID.randomUUID(), "Hello", Optional.of(idempotencyKey));

        assertNotNull(response);
        assertEquals(existingMessage.getId(), response.messageId());
    }
}
```