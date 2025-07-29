package com.example.controller;

import com.example.dto.SendMessageDto;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.example.security.UserPrincipal;
import com.example.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@WithMockUser
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserPrincipal userPrincipal;
    private User sender;
    private UUID senderId = UUID.randomUUID();
    private UUID recipientId = UUID.randomUUID();
    private SendMessageDto sendMessageDto;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(senderId);
        sender.setName("Sender");
        userPrincipal = UserPrincipal.create(sender);

        sendMessageDto = new SendMessageDto();
        sendMessageDto.setRecipientId(recipientId);
        sendMessageDto.setContent("Hello!");
    }

    @Test
    void sendMessage_whenValid_shouldReturnCreated() throws Exception {
        Message sentMessage = new Message();
        sentMessage.setId(UUID.randomUUID());
        sentMessage.setSender(sender);
        sentMessage.setContent(sendMessageDto.getContent());
        
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        sentMessage.setConversation(conversation);

        when(messageService.sendMessage(eq(senderId), any(SendMessageDto.class))).thenReturn(sentMessage);

        mockMvc.perform(post("/messages")
                .with(user(userPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendMessageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.senderId").value(senderId.toString()))
                .andExpect(jsonPath("$.content").value("Hello!"));
    }

    @Test
    void sendMessage_withInvalidDto_shouldReturnBadRequest() throws Exception {
        SendMessageDto invalidDto = new SendMessageDto();
        invalidDto.setRecipientId(null); // Invalid
        invalidDto.setContent(""); // Invalid

        mockMvc.perform(post("/messages")
                .with(user(userPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}