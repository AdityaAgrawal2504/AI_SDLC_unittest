package com.fetchconversations.api.service;

import com.fetchconversations.api.dto.PaginatedConversationsResponseFCA911;
import com.fetchconversations.api.entity.ConversationFCA911;
import com.fetchconversations.api.enums.SortFieldFCA911;
import com.fetchconversations.api.enums.SortOrderFCA911;
import com.fetchconversations.api.repository.ConversationRepositoryFCA911;
import com.fetchconversations.api.util.AuthUtilFCA911;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationServiceFCA911Test {

    @Mock
    private ConversationRepositoryFCA911 conversationRepository;

    @Mock
    private AuthUtilFCA911 authUtil;

    @InjectMocks
    private ConversationServiceFCA911 conversationService;

    private UUID currentUserId;

    @BeforeEach
    void setUp() {
        currentUserId = UUID.randomUUID();
        when(authUtil.getCurrentUserId()).thenReturn(currentUserId);
    }

    @Test
    void getConversations_shouldReturnPaginatedResponse_whenCalledWithValidParameters() {
        // Arrange
        Page<ConversationFCA911> page = new PageImpl<>(Collections.singletonList(new ConversationFCA911()));
        when(conversationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        PaginatedConversationsResponseFCA911 response = conversationService.getConversations(
                "test", SortFieldFCA911.LAST_ACTIVITY, SortOrderFCA911.DESC, 1, 10);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(1);
        assertThat(response.getData()).hasSize(1);
    }

    @Test
    void getConversations_shouldHandleEmptyResult_gracefully() {
        // Arrange
        Page<ConversationFCA911> emptyPage = Page.empty();
        when(conversationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        // Act
        PaginatedConversationsResponseFCA911 response = conversationService.getConversations(
                null, SortFieldFCA911.LAST_ACTIVITY, SortOrderFCA911.DESC, 1, 25);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEmpty();
        assertThat(response.getPagination().getTotalItems()).isZero();
        assertThat(response.getPagination().getTotalPages()).isZero();
    }
}
```
```java
// src/test/java/com/fetchconversations/api/controller/ConversationControllerFCA911Test.java