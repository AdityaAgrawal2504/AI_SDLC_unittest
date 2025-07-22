package com.example.service;

import com.example.dto.GetMessagesResponse_CHAT_2002;
import com.example.dto.MessageDto_CHAT_2012;
import com.example.dto.PaginationDto_CHAT_2013;
import com.example.dto.SendMessageResponse_MSG_3003;
import com.example.entity.ConversationEntity_CHAT_2017;
import com.example.entity.MessageEntity_MSG_3010;
import com.example.entity.UserEntity_UATH_1016;
import com.example.enums.MessageStatus_CHAT_2018;
import com.example.exception.UserNotFoundException_UATH_1014;
import com.example.repository.MessageRepository_MSG_3011;
import com.example.repository.UserRepository_UATH_1017;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for business logic related to sending and retrieving messages.
 */
@Service
@RequiredArgsConstructor
public class MessageService_CHAT_2006 {

    private final MessageRepository_MSG_3011 messageRepository;
    private final UserRepository_UATH_1017 userRepository;
    private final ConversationService_CHAT_2005 conversationService;

    /**
     * Retrieves paginated messages for a given conversation.
     */
    @Transactional(readOnly = true)
    public GetMessagesResponse_CHAT_2002 getMessagesForConversation(UUID userId, UUID conversationId, int page, int limit) {
        conversationService.findAndVerifyConversationParticipant(conversationId, userId);

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("timestamp").descending());
        Page<MessageEntity_MSG_3010> messagePage = messageRepository.findByConversationId(conversationId, pageable);

        List<MessageDto_CHAT_2012> messages = messagePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        PaginationDto_CHAT_2013 pagination = new PaginationDto_CHAT_2013(
                messagePage.getNumber() + 1,
                messagePage.getTotalPages(),
                messagePage.getTotalElements(),
                messagePage.getSize()
        );

        return new GetMessagesResponse_CHAT_2002(messages, pagination);
    }
    
    /**
     * Creates and saves a new message. Handles idempotency.
     */
    @Transactional
    public SendMessageResponse_MSG_3003 sendMessage(UUID senderId, UUID recipientId, String content, Optional<String> idempotencyKey) {
        
        if (idempotencyKey.isPresent()) {
            Optional<MessageEntity_MSG_3010> existingMessage = messageRepository.findByIdempotencyKey(idempotencyKey.get());
            if (existingMessage.isPresent()) {
                MessageEntity_MSG_3010 msg = existingMessage.get();
                return new SendMessageResponse_MSG_3003(msg.getId(), MessageStatus_CHAT_2018.QUEUED_FOR_DELIVERY.name(), msg.getTimestamp());
            }
        }

        UserEntity_UATH_1016 sender = userRepository.findById(senderId)
            .orElseThrow(() -> new UserNotFoundException_UATH_1014("Sender not found."));
        UserEntity_UATH_1016 recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new UserNotFoundException_UATH_1014("The specified recipient user does not exist."));

        ConversationEntity_CHAT_2017 conversation = conversationService.findOrCreateConversation(sender, recipient);

        MessageEntity_MSG_3010 message = new MessageEntity_MSG_3010(conversation, sender, content, idempotencyKey.orElse(null));
        MessageEntity_MSG_3010 savedMessage = messageRepository.save(message);

        // Update the conversation's last message
        conversation.setLastMessage(savedMessage);

        // In a real system, you would now push this message to a queue (e.g., RabbitMQ, Kafka)
        // for real-time delivery via gRPC/WebSockets.

        return new SendMessageResponse_MSG_3003(savedMessage.getId(), MessageStatus_CHAT_2018.QUEUED_FOR_DELIVERY.name(), savedMessage.getTimestamp());
    }

    private MessageDto_CHAT_2012 convertToDto(MessageEntity_MSG_3010 entity) {
        return new MessageDto_CHAT_2012(
                entity.getId(),
                entity.getConversation().getId(),
                entity.getSender().getId(),
                entity.getContent(),
                entity.getTimestamp(),
                entity.getStatus()
        );
    }
}
```
```java
//
// Filename: src/main/java/com/example/util/StructuredLogger_UTIL_9999.java
//