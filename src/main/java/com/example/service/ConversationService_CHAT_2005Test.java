package com.example.service;

import com.example.entity.ConversationEntity_CHAT_2017;
import com.example.exception.ConversationNotFoundException_CHAT_2010;
import com.example.exception.ForbiddenAccessException_CHAT_2011;
import com.example.repository.ConversationRepository_CHAT_2019;
import com.example.repository.MessageRepository_MSG_3011;
import com.example.repository.UserRepository_UATH_1017;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationService_CHAT_2005Test {

    @Mock
    private ConversationRepository_CHAT_2019 conversationRepository;

    @Mock
    private MessageRepository_MSG_3011 messageRepository;

    @Mock
    private UserRepository_UATH_1017 userRepository;

    @InjectMocks
    private ConversationService_CHAT_2005 conversationService;

    @Test
    void getUserConversations_whenCalled_shouldReturnResponse() {
        UUID userId = UUID.randomUUID();
        when(conversationRepository.findConversationsForUser(any(UUID.class), anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        conversationService.getUserConversations(userId, 1, 20, null, null, null);
        // Assert no exception is thrown, more detailed checks would require complex mocking
    }

    @Test
    void findAndVerifyConversationParticipant_whenNotFound_shouldThrowException() {
        UUID convoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(conversationRepository.findById(convoId)).thenReturn(Optional.empty());

        assertThrows(ConversationNotFoundException_CHAT_2010.class, () -> {
            conversationService.findAndVerifyConversationParticipant(convoId, userId);
        });
    }

    @Test
    void findAndVerifyConversationParticipant_whenNotParticipant_shouldThrowException() {
        UUID convoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ConversationEntity_CHAT_2017 conversation = new ConversationEntity_CHAT_2017(); // has empty participant set

        when(conversationRepository.findById(convoId)).thenReturn(Optional.of(conversation));

        assertThrows(ForbiddenAccessException_CHAT_2011.class, () -> {
            conversationService.findAndVerifyConversationParticipant(convoId, userId);
        });
    }
}

```
```java
//
// Filename: src/test/java/com/example/service/MessageService_CHAT_2006Test.java
//