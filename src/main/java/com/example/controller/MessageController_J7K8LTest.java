src/test/java/com/example/controller/MessageController_J7K8LTest.java
package com.example.controller;

import com.example.config.JwtAuthFilter_K9L0M;
import com.example.config.SecurityConfig_H7I8J;
import com.example.dto.MessageDto_S3T4U;
import com.example.dto.SendMessageDto_P1Q2R;
import com.example.model.User_M1N2O;
import com.example.provider.interfaces.IJwtProvider_G5H6I;
import com.example.service.interfaces.IMessageService_B3C4D;
import com.example.service.interfaces.IUserService_D7E8F;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController_J7K8L.class)
@Import({SecurityConfig_H7I8J.class, JwtAuthFilter_K9L0M.class})
class MessageController_J7K8LTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IMessageService_B3C4D messageService;

    @MockBean
    private IUserService_D7E8F userService;

    @MockBean
    private IJwtProvider_G5H6I jwtProvider;

    private User_M1N2O user;
    private SendMessageDto_P1Q2R sendMessageDto;
    private MessageDto_S3T4U messageDto;

    @BeforeEach
    void setUp() {
        user = new User_M1N2O();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber("+15550000001");

        sendMessageDto = new SendMessageDto_P1Q2R(
                null,
                "+15550000002",
                "Hello World!"
        );

        messageDto = new MessageDto_S3T4U(
                UUID.randomUUID(),
                UUID.randomUUID(),
                user.getId(),
                "Hello World!",
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser
    void sendMessage_Success() throws Exception {
        when(userService.findById(any())).thenReturn(user);
        when(messageService.sendMessage(eq(user), any(SendMessageDto_P1Q2R.class))).thenReturn(messageDto);

        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendMessageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(messageDto.getId().toString()))
                .andExpect(jsonPath("$.content").value("Hello World!"));
    }

    @Test
    @WithMockUser
    void sendMessage_InvalidBody_NoTarget() throws Exception {
        when(userService.findById(any())).thenReturn(user);
        SendMessageDto_P1Q2R invalidDto = new SendMessageDto_P1Q2R(null, null, "content");

        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void sendMessage_InvalidBody_BothTargets() throws Exception {
        when(userService.findById(any())).thenReturn(user);
        SendMessageDto_P1Q2R invalidDto = new SendMessageDto_P1Q2R(UUID.randomUUID(), "+15551234567", "content");

        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void sendMessage_InvalidBody_NoContent() throws Exception {
        when(userService.findById(any())).thenReturn(user);
        SendMessageDto_P1Q2R invalidDto = new SendMessageDto_P1Q2R(null, "+15551234567", "");

        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}