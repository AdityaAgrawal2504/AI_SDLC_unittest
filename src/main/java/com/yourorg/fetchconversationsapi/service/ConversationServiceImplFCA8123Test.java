package com.yourorg.fetchconversationsapi.service;

import com.yourorg.fetchconversationsapi.dto.PaginatedConversationsResponseFCA8123;
import com.yourorg.fetchconversationsapi.enums.ConversationSortFieldFCA8123;
import com.yourorg.fetchconversationsapi.enums.SortOrderFCA8123;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the ConversationServiceImplFCA8123 class.
 */
@ExtendWith(MockitoExtension.class)
class ConversationServiceImplFCA8123Test {

    @InjectMocks
    private ConversationServiceImplFCA8123 conversationService;

    /**
     * Tests the happy path of the fetchConversations method.
     */
    @Test
    void fetchConversations_shouldReturnPaginatedResponse() {
        // Given
        String query = "hello";
        ConversationSortFieldFCA8123 sort = ConversationSortFieldFCA8123.LAST_ACTIVITY;
        SortOrderFCA8123 order = SortOrderFCA8123.DESC;
        int page = 1;
        int pageSize = 10;

        // When
        PaginatedConversationsResponseFCA8123 response = conversationService.fetchConversations(query, sort, order, page, pageSize);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData()).hasSize(pageSize);
        assertThat(response.getPagination()).isNotNull();
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(page);
        assertThat(response.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(42);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(5); // ceil(42.0 / 10)
    }

    /**
     * Tests that the service returns a correctly structured response even with minimal parameters.
     */
    @Test
    void fetchConversations_withDefaultParameters_shouldReturnPaginatedResponse() {
        // Given
        int page = 1;
        int pageSize = 25;

        // When
        PaginatedConversationsResponseFCA8123 response = conversationService.fetchConversations(null, ConversationSortFieldFCA8123.LAST_ACTIVITY, SortOrderFCA8123.DESC, page, pageSize);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(pageSize);
        assertThat(response.getPagination()).isNotNull();
        assertThat(response.getPagination().getCurrentPage()).isEqualTo(page);
        assertThat(response.getPagination().getPageSize()).isEqualTo(pageSize);
    }
}
```
src/test/java/com/yourorg/fetchconversationsapi/controller/ConversationControllerFCA8123Test.java
```java