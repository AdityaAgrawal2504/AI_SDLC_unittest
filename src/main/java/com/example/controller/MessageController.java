package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.SendMessageDto;
import com.example.model.Message;
import com.example.security.UserPrincipal;
import com.example.service.IMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Handles POST /messages. Sends a message to a recipient.
     * @param currentUser The authenticated sender.
     * @param sendMessageDto The message details.
     * @return The created message.
     */
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody SendMessageDto sendMessageDto) {
        
        Message sentMessage = messageService.sendMessage(currentUser.getId(), sendMessageDto);
        MessageDto responseDto = convertToDto(sentMessage);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
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