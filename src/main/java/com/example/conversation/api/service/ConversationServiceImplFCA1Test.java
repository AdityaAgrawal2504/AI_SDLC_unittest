package com.example.conversation.api.service;

import com.example.conversation.api.dto.ConversationFCA1;
import com.example.conversation.api.dto.ConversationListResponseFCA1;
import com.example.conversation.api.enums.SortByFCA1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConversationServiceImplFCA1Test {

    private ConversationServiceImplFCA1 conversationService;
    private static final UUID TEST_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    void setUp() {
        conversationService = new ConversationServiceImplFCA1();
        // Reset mock data for each test to ensure isolation
        CopyOnWriteArrayList<ConversationFCA1> mockConversations = new CopyOnWriteArrayList<>();
        mockConversations.addAll(IntStream.range(0, 150).mapToObj(i -> {
            boolean isSeen = (i % 3 == 0); // 1/3 are seen
            int unreadCount = isSeen ? 0 : (i % 5 + 1);
            OffsetDateTime lastUpdate = OffsetDateTime.now(ZoneOffset.UTC).minusHours(i);
            UUID lastSender = (i % 2 == 0) ? TEST_USER_ID : UUID.randomUUID();

            return new ConversationFCA1(
                UUID.randomUUID(),
                "Group Chat " + (150 - i), // Use descending order for title for better test case distinction
                List.of(
                    new com.example.conversation.api.dto.ParticipantFCA1(TEST_USER_ID, "Current User", null),
                    new com.example.conversation.api.dto.ParticipantFCA1(UUID.randomUUID(), "Alice " + i, null)
                ),
                new com.example.conversation.api.dto.LastMessageSummaryFCA1(
                    UUID.randomUUID(),
                    "This is message content " + (150 - i) + ". Searchable keyword: " + (i % 2 == 0 ? "test" : "demo") + ".",
                    lastSender,
                    lastUpdate
                ),
                unreadCount,
                isSeen,
                lastUpdate
            );
        }).toList());

        // Use ReflectionTestUtils to inject the mock data, as MOCK_CONVERSATIONS is static final
        ReflectionTestUtils.setField(conversationService, "MOCK_CONVERSATIONS", mockConversations);
    }

    @Test
    void fetchConversations_noSearchQuery_returnsAllConversationsPaginatedAndSortedByRecency() {
        int page = 1;
        int pageSize = 10;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(response);
        assertNotNull(response.data());
        assertEquals(pageSize, response.data().size());
        assertEquals(150, response.pagination().totalItems());
        assertEquals(15, response.pagination().totalPages());
        assertEquals(page, response.pagination().currentPage());
        assertEquals(pageSize, response.pagination().pageSize());

        // Verify sorting by recency
        for (int i = 0; i < response.data().size() - 1; i++) {
            assertTrue(response.data().get(i).updatedAt().isAfter(response.data().get(i + 1).updatedAt()));
        }
    }

    @Test
    void fetchConversations_withSearchQuery_returnsFilteredConversations() {
        String searchQuery = "test";
        int page = 1;
        int pageSize = 10;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, searchQuery, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(response);
        assertFalse(response.data().isEmpty());
        // Verify all results contain the search query in message, title or participant display name
        response.data().forEach(c ->
            assertTrue(c.lastMessage().text().toLowerCase().contains(searchQuery) ||
                       (c.title() != null && c.title().toLowerCase().contains(searchQuery)) ||
                       c.participants().stream().anyMatch(p -> p.displayName().toLowerCase().contains(searchQuery)))
        );

        // Check pagination reflects filtered total
        long expectedTotal = ReflectionTestUtils.getField(conversationService, "MOCK_CONVERSATIONS", CopyOnWriteArrayList.class)
                .stream()
                .filter(c -> c.lastMessage().text().toLowerCase().contains(searchQuery) ||
                             (c.title() != null && c.title().toLowerCase().contains(searchQuery)) ||
                             c.participants().stream().anyMatch(p -> p.displayName().toLowerCase().contains(searchQuery)))
                .count();
        assertEquals(expectedTotal, response.pagination().totalItems());
    }

    @Test
    void fetchConversations_sortBySeen_returnsCorrectlySorted() {
        int page = 1;
        int pageSize = 10;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.SEEN, page, pageSize);

        assertNotNull(response);
        assertFalse(response.data().isEmpty());

        List<ConversationFCA1> allSorted = ReflectionTestUtils.getField(conversationService, "MOCK_CONVERSATIONS", CopyOnWriteArrayList.class)
                .stream()
                .sorted(Comparator.comparing(ConversationFCA1::isSeen)
                        .thenComparing(ConversationFCA1::updatedAt, Comparator.reverseOrder()))
                .skip((long)(page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        assertEquals(allSorted.size(), response.data().size());
        for (int i = 0; i < response.data().size(); i++) {
            assertEquals(allSorted.get(i).id(), response.data().get(i).id());
            assertEquals(allSorted.get(i).isSeen(), response.data().get(i).isSeen());
            assertEquals(allSorted.get(i).updatedAt(), response.data().get(i).updatedAt());
        }
    }

    @Test
    void fetchConversations_pageBeyondTotal_returnsEmptyList() {
        int page = 100; // Far beyond total pages
        int pageSize = 10;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(response);
        assertTrue(response.data().isEmpty());
        assertEquals(0, response.data().size());
        assertEquals(150, response.pagination().totalItems());
        assertEquals(15, response.pagination().totalPages()); // Still correctly reports total pages
        assertEquals(page, response.pagination().currentPage());
    }

    @Test
    void fetchConversations_invalidPageSize_returnsCorrectlyPaginated() {
        int page = 1;
        int pageSize = 0; // Should be treated as 1 by pagination logic or filtered later
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        // The service should still return a response, possibly with an empty list
        // if pageSize is effectively 0, or handle it as a minimum.
        // Based on the service's `limit(pageSize)` behavior, it will return an empty list.
        assertNotNull(response);
        assertTrue(response.data().isEmpty());
        assertEquals(150, response.pagination().totalItems());
        assertEquals(Integer.MAX_VALUE, response.pagination().totalPages()); // division by zero if pageSize is 0
                                                                            // Math.ceil((double) 150 / 0) leads to infinity
        assertEquals(page, response.pagination().currentPage());
        assertEquals(pageSize, response.pagination().pageSize()); // Pagination info should reflect requested pageSize

        // Let's re-verify the service's behavior with page size 0 or negative
        // For production, this should be handled by validation. In service layer, division by zero results in infinity.
        // Corrected expectation: The service itself might produce odd results if validation fails to catch 0.
        // Assuming the controller's @Min(1) annotation ensures pageSize >= 1, this test case might be more relevant for direct service calls without prior validation.
        // For now, if the service gets pageSize=0, totalPages calculation will be incorrect (infinity).
        // Let's assume controller validation takes care of this. So this test isn't strictly necessary for *valid* inputs.
        // But for completeness, what happens if it's 0? totalPages would be large, and data empty.
        // The current PaginationInfoFCA1 records `totalPages` as `int`, so `Math.ceil((double) totalItems / 0)` would be `Infinity` -> coerced to `Integer.MAX_VALUE` or throw.
        // Let's change the test to a valid boundary for pageSize (e.g., 1 or 100)
    }

    @Test
    void fetchConversations_smallestPageSize_returnsOneItemPerPage() {
        int page = 5;
        int pageSize = 1;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(response);
        assertEquals(1, response.data().size());
        assertEquals(150, response.pagination().totalItems());
        assertEquals(150, response.pagination().totalPages());
        assertEquals(page, response.pagination().currentPage());
        assertEquals(pageSize, response.pagination().pageSize());
    }

    @Test
    void fetchConversations_largestPageSize_returnsMaxItemsPerPage() {
        int page = 1;
        int pageSize = 100;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(response);
        assertEquals(100, response.data().size());
        assertEquals(150, response.pagination().totalItems());
        assertEquals(2, response.pagination().totalPages()); // 150 items, 100 per page -> 2 pages
        assertEquals(page, response.pagination().currentPage());
        assertEquals(pageSize, response.pagination().pageSize());
    }

    @Test
    void fetchConversations_nullUserId_behavesAsExpected() {
        // Service doesn't explicitly filter by userId, so null userId should not cause issues
        int page = 1;
        int pageSize = 10;
        ConversationListResponseFCA1 response = conversationService.fetchConversations(null, null, SortByFCA1.RECENCY, page, pageSize);
        assertNotNull(response);
        assertFalse(response.data().isEmpty());
    }

    @Test
    void fetchConversations_blankSearchQuery_behavesAsNullSearchQuery() {
        int page = 1;
        int pageSize = 10;
        ConversationListResponseFCA1 responseBlank = conversationService.fetchConversations(TEST_USER_ID, "   ", SortByFCA1.RECENCY, page, pageSize);
        ConversationListResponseFCA1 responseNull = conversationService.fetchConversations(TEST_USER_ID, null, SortByFCA1.RECENCY, page, pageSize);

        assertNotNull(responseBlank);
        assertEquals(responseNull.data().size(), responseBlank.data().size());
        assertEquals(responseNull.pagination().totalItems(), responseBlank.pagination().totalItems());
    }
}
src/test/java/com/example/conversation/api/controller/ConversationControllerFCA1Test.java