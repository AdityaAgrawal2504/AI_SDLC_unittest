package com.example.controller;

import com.example.dto.ListConversationsResponse_CHAT_2003;
import com.example.security.JwtAuthenticationFilter_SEC_33CC;
import com.example.service.ConversationService_CHAT_2005;
import com.example.service.MessageService_CHAT_2006;
import com.example.util.StructuredLogger_UTIL_9999;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = ConversationController_CHAT_2001.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter_SEC_33CC.class)
)
class ConversationController_CHAT_2001Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationService_CHAT_2005 conversationService;
    
    @MockBean
    private MessageService_CHAT_2006 messageService;

    @MockBean
    private StructuredLogger_UTIL_9999 logger;

    @Test
    @WithMockUser(username = "a89a19a9-823c-4a30-9a99-2782075b2d23")
    void listConversations_whenAuthenticated_shouldReturnOk() throws Exception {
        ListConversationsResponse_CHAT_2003 response = new ListConversationsResponse_CHAT_2003(Collections.emptyList(), null);
        when(conversationService.getUserConversations(any(UUID.class), anyInt(), anyInt(), any(), any(), anyString()))
            .thenReturn(response);

        mockMvc.perform(get("/conversations"))
                .andExpect(status().isOk());
    }

    @Test
    void listConversations_whenUnauthenticated_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/conversations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "a89a19a9-823c-4a30-9a99-2782075b2d23")
    void listConversations_withInvalidPageParam_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/conversations?page=0"))
                .andExpect(status().isBadRequest());
    }
}
```
```java
//
// Filename: src/test/java/com/example/service/UserService_UATH_1009Test.java
//