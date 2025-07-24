package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.PaginatedMessagesResponse;
import com.example.messagingapp.dto.SendMessageRequest;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.MessageStatus;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import com.example.messagingapp.service.MessageService;
import com.example.messagingapp.service.UserService;
import com.example.messagingapp.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    /**
     * Persists a new message to the database. In a real system, this might enqueue the message.
     * @param senderId The ID of the user sending the message.
     * @param request DTO containing recipient ID and message content.
     * @return The initial state of the message DTO.
     */
    @Override
    @Transactional
    public MessageResponse sendMessage(UUID senderId, SendMessageRequest request) {
        User sender = userService.findById(senderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SENDER_NOT_FOUND", "Sender user not found."));

        User recipient = userService.findById(request.getRecipientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "RECIPIENT_NOT_FOUND", "Recipient user not found."));

        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(request.getContent())
                .status(MessageStatus.SENT)
                .build();
        
        // In a real-world scenario with a queue, you would do:
        // queueService.enqueue("message-delivery", message);
        // The repository save would happen in a queue consumer.
        // For this implementation, we save directly.
        Message savedMessage = messageRepository.save(message);

        return DtoMapper.toMessageResponse(savedMessage);
    }

    /**
     * Lists messages for a specific user with pagination, sorting, and searching.
     * @param userId The ID of the user.
     * @param page The page number (1-based).
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort on.
     * @param sortOrder The sort direction ('asc' or 'desc').
     * @param search A search term for message content.
     * @return A paginated response of message DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessagesResponse listMessages(UUID userId, int page, int pageSize, String sortBy, String sortOrder, String search) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(direction, sortBy));

        Page<Message> messagePage = messageRepository.findMessagesForUser(userId, search, pageable);

        List<MessageResponse> messageResponses = messagePage.getContent().stream()
                .map(DtoMapper::toMessageResponse)
                .collect(Collectors.toList());

        PaginatedMessagesResponse.PaginationInfo paginationInfo = PaginatedMessagesResponse.PaginationInfo.builder()
                .totalItems(messagePage.getTotalElements())
                .totalPages(messagePage.getTotalPages())
                .currentPage(messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .build();

        return PaginatedMessagesResponse.builder()
                .data(messageResponses)
                .pagination(paginationInfo)
                .build();
    }
    
    /**
     * Marks a specific message as read if the user is the recipient.
     * @param userId The ID of the user marking the message.
     * @param messageId The ID of the message.
     */
    @Override
    @Transactional
    public void markMessageAsRead(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "MESSAGE_NOT_FOUND", "Message not found."));

        // Ensure the user is the recipient of the message
        if (!message.getRecipient().getId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "CANNOT_ACCESS_RESOURCE", "User is not the recipient of this message.");
        }

        // Only update if the status is not already READ
        if (message.getStatus() != MessageStatus.READ) {
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        }
    }
}