package com.example.controller;

import com.example.dto.MarkAsReadDto;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.security.UserPrincipal;
import com.example.service.IConversationService;
import com.example.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConversationController.class)
@WithMockUser
public class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IConversationService conversationService;

    @MockBean
    private IMessageService messageService;

    private UserPrincipal userPrincipal;
    private User user;
    private Conversation conversation;
    private UUID conversationId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        userPrincipal = UserPrincipal.create(user);
        
        conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setParticipants(Set.of(user));
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void listConversations_shouldReturnPaginatedConversations() throws Exception {
        when(conversationService.getConversationsForUser(eq(userId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(conversation)));

        mockMvc.perform(get("/conversations").with(user(userPrincipal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(conversationId.toString()))
                .andExpect(jsonPath("$.pagination.currentPage").value(1));
    }
    
    @Test
    void getConversationMessages_whenParticipant_shouldReturnMessages() throws Exception {
        when(conversationService.isUserParticipant(conversationId, userId)).thenReturn(true);
        when(messageService.getMessagesForConversation(eq(conversationId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        
        mockMvc.perform(get("/conversations/{conversationId}/messages", conversationId).with(user(userPrincipal)))
                .andExpect(status().isOk());
    }
    
    @Test
    void getConversationMessages_whenNotParticipant_shouldReturnForbidden() throws Exception {
        when(conversationService.isUserParticipant(conversationId, userId)).thenReturn(false);
        
        mockMvc.perform(get("/conversations/{conversationId}/messages", conversationId).with(user(userPrincipal)))
                .andExpect(status().isForbidden());
    }

    @Test
    void markConversationAsRead_whenParticipant_shouldReturnNoContent() throws Exception {
        when(conversationService.isUserParticipant(conversationId, userId)).thenReturn(true);
        when(conversationService.findById(conversationId)).thenReturn(java.util.Optional.of(conversation));
        
        MarkAsReadDto dto = new MarkAsReadDto();
        dto.setLastReadTimestamp(OffsetDateTime.now());

        mockMvc.perform(put("/conversations/{conversationId}/read", conversationId)
                .with(user(userPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }
}