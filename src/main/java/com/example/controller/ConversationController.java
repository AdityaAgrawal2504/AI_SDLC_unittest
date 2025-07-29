package com.example.controller;

import com.example.dto.*;
import com.example.exception.ApiException;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.security.UserPrincipal;
import com.example.service.IConversationService;
import com.example.service.IMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final IConversationService conversationService;
    private final IMessageService messageService;

    public ConversationController(IConversationService conversationService, IMessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    /**
     * Handles GET /conversations. Retrieves a paginated list of user's conversations.
     */
    @GetMapping
    public ResponseEntity<PaginatedConversationResponseDto> listConversations(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "recency") String sortBy) {

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Conversation> conversationPage = conversationService.getConversationsForUser(currentUser.getId(), pageable);
        
        List<ConversationDto> dtos = conversationPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                conversationPage.getNumber() + 1,
                conversationPage.getSize(),
                conversationPage.getTotalElements(),
                conversationPage.getTotalPages());

        return ResponseEntity.ok(new PaginatedConversationResponseDto(dtos, paginationInfo));
    }

    /**
     * Handles GET /conversations/{conversationId}/messages. Retrieves messages for a conversation.
     */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<PaginatedMessageResponseDto> getConversationMessages(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit) {

        if (!conversationService.isUserParticipant(conversationId, currentUser.getId())) {
            throw new AccessDeniedException("User is not a participant in this conversation.");
        }
        
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Message> messagePage = messageService.getMessagesForConversation(conversationId, pageable);

        List<MessageDto> dtos = messagePage.getContent().stream()
                .map(this::convertMessageToDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                messagePage.getNumber() + 1,
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.getTotalPages());

        return ResponseEntity.ok(new PaginatedMessageResponseDto(dtos, paginationInfo));
    }

    /**
     * Handles PUT /conversations/{conversationId}/read. Marks a conversation as read.
     */
    @PutMapping("/{conversationId}/read")
    public ResponseEntity<Void> markConversationAsRead(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable UUID conversationId,
            @RequestBody MarkAsReadDto markAsReadDto) {
        
        if (!conversationService.isUserParticipant(conversationId, currentUser.getId())) {
            throw new AccessDeniedException("User is not a participant in this conversation.");
        }
        
        conversationService.findById(conversationId).orElseThrow(() -> new ApiException("Conversation not found.", HttpStatus.NOT_FOUND));

        conversationService.markConversationAsRead(currentUser.getId(), conversationId, markAsReadDto.getLastReadTimestamp());
        return ResponseEntity.noContent().build();
    }
    
    private ConversationDto convertToDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        
        List<UserResponseDto> participants = conversation.getParticipants().stream().map(user -> {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setCreatedAt(user.getCreatedAt());
            return userDto;
        }).collect(Collectors.toList());
        dto.setParticipants(participants);
        
        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            dto.setLastMessage(convertMessageToDto(conversation.getMessages().get(0)));
        }
        
        // Unread count logic would be more complex, this is a placeholder
        dto.setUnreadCount(0); 
        
        return dto;
    }

    private MessageDto convertMessageToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}