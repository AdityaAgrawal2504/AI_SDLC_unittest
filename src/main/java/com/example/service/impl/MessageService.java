package com.example.service.impl;

import com.example.dto.MessageDto;
import com.example.dto.SendMessageDto;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IMessageRepository;
import com.example.repository.IUserRepository;
import com.example.service.IConversationService;
import com.example.service.IMessageService;
import com.example.service.IWebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MessageService implements IMessageService {

    private static final Logger log = LogManager.getLogger(MessageService.class);

    private final IMessageRepository messageRepository;
    private final IConversationService conversationService;
    private final IUserRepository userRepository;
    private final IWebSocketService webSocketService;
    private final ObjectMapper objectMapper;

    public MessageService(IMessageRepository messageRepository, IConversationService conversationService,
                          IUserRepository userRepository, IWebSocketService webSocketService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        this.userRepository = userRepository;
        this.webSocketService = webSocketService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Creates and sends a new message.
     * @param senderId The ID of the message sender.
     * @param sendMessageDto DTO containing message details.
     * @return The created Message entity.
     */
    @Override
    @Transactional
    public Message sendMessage(UUID senderId, SendMessageDto sendMessageDto) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", senderId));
        
        // Ensure recipient exists
        if (!userRepository.existsById(sendMessageDto.getRecipientId())) {
             throw new ResourceNotFoundException("User", "id", sendMessageDto.getRecipientId());
        }

        Conversation conversation = conversationService.findOrCreateConversation(senderId, sendMessageDto.getRecipientId());

        Message message = new Message();
        message.setSender(sender);
        message.setContent(sendMessageDto.getContent());
        message.setConversation(conversation);

        Message savedMessage = messageRepository.save(message);

        // Push real-time event to recipient
        try {
            MessageDto messageDto = convertToDto(savedMessage);
            String payload = objectMapper.writeValueAsString(messageDto);
            webSocketService.sendToUser(sendMessageDto.getRecipientId(), payload);
        } catch (JsonProcessingException e) {
            log.error("Error serializing message DTO for WebSocket push", e);
        }

        return savedMessage;
    }

    /**
     * Retrieves a paginated list of messages for a conversation.
     * @param conversationId The conversation's ID.
     * @param pageable Pagination information.
     * @return A Page of Messages.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Message> getMessagesForConversation(UUID conversationId, Pageable pageable) {
        return messageRepository.findByConversationId(conversationId, pageable);
    }
    
    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}