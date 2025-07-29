package com.example.service;

import com.example.dto.SendMessageDto;
import com.example.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IMessageService {
    Message sendMessage(UUID senderId, SendMessageDto sendMessageDto);
    Page<Message> getMessagesForConversation(UUID conversationId, Pageable pageable);
}