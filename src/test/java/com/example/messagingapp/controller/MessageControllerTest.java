package com.example.messagingapp.controller;

import com.example.messagingapp.dto.MessageResponse;
import com.example.messagingapp.dto.PaginatedMessagesResponse;
import com.example.messagingapp.dto.SendMessageRequest;
import com.example.messagingapp.model.MessageStatus;
import com.example.messagingapp.security.AuthenticatedUser;
import com.example.messagingapp.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@Import(TestSecurityConfig.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    private AuthenticatedUser testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new AuthenticatedUser(testUserId, "+1234567890");
    }
    
    /**
     * Tests message sending with valid input.
     */
    @Test
    @WithMockUser
    void sendMessage_shouldReturnAccepted() throws Exception {
        UUID recipientId = UUID.randomUUID();
        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello!");
        MessageResponse response = MessageResponse.builder()
                .id(UUID.randomUUID())
                .senderId(testUserId)
                .recipientId(recipientId)
                .content("Hello!")
                .status(MessageStatus.SENT)
                .createdAt(OffsetDateTime.now())
                .build();

        when(messageService.sendMessage(eq(testUserId), any(SendMessageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/messages")
                .with(user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.senderId").value(testUserId.toString()));
    }

    /**
     * Tests listing messages for the authenticated user.
     */
    @Test
    @WithMockUser
    void listMessages_shouldReturnPaginatedResponse() throws Exception {
        MessageResponse msgResponse = MessageResponse.builder().id(UUID.randomUUID()).build();
        PaginatedMessagesResponse.PaginationInfo paginationInfo = new PaginatedMessagesResponse.PaginationInfo(1, 1, 1, 20);
        PaginatedMessagesResponse response = new PaginatedMessagesResponse(Collections.singletonList(msgResponse), paginationInfo);

        when(messageService.listMessages(eq(testUserId), anyInt(), anyInt(), anyString(), anyString(), any())).thenReturn(response);

        mockMvc.perform(get("/messages")
                .with(user(testUser))
                .param("page", "1")
                .param("pageSize", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pagination.currentPage").value(1));
    }

    /**
     * Tests marking a message as read.
     */
    @Test
    @WithMockUser
    void markMessageRead_shouldReturnOk() throws Exception {
        UUID messageId = UUID.randomUUID();

        mockMvc.perform(post("/messages/{messageId}/read", messageId)
                .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Message marked as read."));
    }
}