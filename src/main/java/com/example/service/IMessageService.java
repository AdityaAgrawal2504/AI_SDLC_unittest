package com.example.service;

import com.example.dto.MessageResponse;
import com.example.dto.SendMessageRequest;
import java.util.UUID;

public interface IMessageService {
    MessageResponse sendMessage(UUID senderId, SendMessageRequest sendMessageRequest);
}