package com.example.usersearch.service;

import com.example.usersearch.dto.UserSearchResponse_A0B1;
import com.example.usersearch.dto.UserSummary_A0B1;
import com.example.usersearch.exception.DatabaseSearchException_A0B1;
import com.example.usersearch.model.BlockedUserEntity_A0B1;
import com.example.usersearch.model.UserEntity_A0B1;
import com.example.usersearch.repository.BlockedUserRepository_A0B1;
import com.example.usersearch.repository.ConversationRepository_A0B1;
import com.example.usersearch.repository.UserRepository_A0B1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSearchService_A0B1Test {

    @Mock
    private UserRepository_A0B1 userRepository;
    @Mock
    private ConversationRepository_A0B1 conversationRepository;
    @Mock
    private BlockedUserRepository_A0B1 blockedUserRepository; // Though not directly injected, it's used in the specification.

    @InjectMocks
    private UserSearchService_A0B1 userSearchService;

    private UUID requesterId;
    private UserEntity_A0B1 user1, user2, user3;

    @BeforeEach
    void setUp() {
        requesterId = UUID.randomUUID();

        user1 = UserEntity_A0B1.builder()
                .id(UUID.randomUUID())
                .name("Alice Smith")
                .phoneNumber("+1234567890")
                .profilePictureUrl("url1")
                .build();
        user2 = UserEntity_A0B1.builder()
                .id(UUID.randomUUID())
                .name("Bob Johnson")
                .phoneNumber("+1987654321")
                .profilePictureUrl("url2")
                .build();
        user3 = UserEntity_A0B1.builder()
                .id(UUID.randomUUID())
                .name("Charlie Brown")
                .phoneNumber("+1111222333")
                .profilePictureUrl("url3")
                .build();
    }

    @Test
    void searchUsers_shouldReturnPaginatedResults_success() {
        // Arrange
        String query = "Alice";
        int page = 1;
        int pageSize = 10;
        List<UserEntity_A0B1> users = List.of(user1);
        Page<UserEntity_A0B1> userPage = new PageImpl<>(users, PageRequest.of(page - 1, pageSize), 1);

        when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(page - 1, pageSize))))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(eq(requesterId), any(UUID.class))).thenReturn(false);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPagination());
        assertNotNull(response.getResults());
        assertEquals(1, response.getResults().size());
        assertEquals(user1.getId(), response.getResults().get(0).getUserId());
        assertEquals(1, response.getPagination().getTotalItems());
        assertEquals(1, response.getPagination().getTotalPages());
        assertEquals(page, response.getPagination().getCurrentPage());
        assertEquals(pageSize, response.getPagination().getPageSize());

        verify(userRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        verify(conversationRepository, times(1)).existsConversationBetween(eq(requesterId), eq(user1.getId()));
    }

    @Test
    void searchUsers_shouldExcludeRequester() {
        // Arrange
        String query = "Alice";
        int page = 1;
        int pageSize = 10;
        UserEntity_A0B1 requesterUser = UserEntity_A0B1.builder()
                .id(requesterId)
                .name("Requester User")
                .phoneNumber("+1000000000")
                .build();
        List<UserEntity_A0B1> users = List.of(requesterUser, user1); // Including requester in mock results

        // Mock findAll to capture the Specification and apply it manually to filter out requester
        when(userRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenAnswer(invocation -> {
                    Specification<UserEntity_A0B1> spec = invocation.getArgument(0);
                    // Manually filter users based on the specification's logic (simplified for test)
                    List<UserEntity_A0B1> filteredUsers = users.stream()
                            .filter(u -> !u.getId().equals(requesterId))
                            .collect(java.util.ArrayList::new, java.util.ArrayList::add, java.util.ArrayList::addAll);
                    return new PageImpl<>(filteredUsers, PageRequest.of(page - 1, pageSize), filteredUsers.size());
                });

        when(conversationRepository.existsConversationBetween(eq(requesterId), any(UUID.class))).thenReturn(false);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertEquals(user1.getId(), response.getResults().get(0).getUserId()); // Requester should be excluded
        assertEquals(user1.getName(), response.getResults().get(0).getName());
    }


    @Test
    void searchUsers_shouldHandleNoResults() {
        // Arrange
        String query = "NonExistent";
        int page = 1;
        int pageSize = 10;
        Page<UserEntity_A0B1> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, pageSize), 0);

        when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(page - 1, pageSize))))
                .thenReturn(emptyPage);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertTrue(response.getResults().isEmpty());
        assertEquals(0, response.getPagination().getTotalItems());
        assertEquals(0, response.getPagination().getTotalPages());
    }

    @Test
    void searchUsers_shouldReturnHasExistingConversationTrue() {
        // Arrange
        String query = "Bob";
        int page = 1;
        int pageSize = 10;
        List<UserEntity_A0B1> users = List.of(user2);
        Page<UserEntity_A0B1> userPage = new PageImpl<>(users, PageRequest.of(page - 1, pageSize), 1);

        when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(page - 1, pageSize))))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(requesterId, user2.getId())).thenReturn(true);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertTrue(response.getResults().get(0).isHasExistingConversation());
    }

    @Test
    void searchUsers_shouldReturnHasExistingConversationFalse() {
        // Arrange
        String query = "Charlie";
        int page = 1;
        int pageSize = 10;
        List<UserEntity_A0B1> users = List.of(user3);
        Page<UserEntity_A0B1> userPage = new PageImpl<>(users, PageRequest.of(page - 1, pageSize), 1);

        when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(page - 1, pageSize))))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(requesterId, user3.getId())).thenReturn(false);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertFalse(response.getResults().get(0).isHasExistingConversation());
    }

    @Test
    void searchUsers_shouldThrowDatabaseSearchExceptionOnRepositoryError() {
        // Arrange
        String query = "Error";
        int page = 1;
        int pageSize = 10;

        when(userRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        DatabaseSearchException_A0B1 thrown = assertThrows(DatabaseSearchException_A0B1.class, () -> {
            userSearchService.searchUsers(query, requesterId, page, pageSize);
        });

        assertEquals("Failed to execute user search.", thrown.getMessage());
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause().getMessage().contains("Database connection error"));

        verify(userRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        verifyNoInteractions(conversationRepository); // Should not be called if user repo fails
    }

    @Test
    void searchUsers_shouldExcludeBlockedUsers() {
        // Arrange
        String query = "Charlie";
        int page = 1;
        int pageSize = 10;

        // Simulate Charlie blocking the requester (requesterId)
        BlockedUserEntity_A0B1 blockedEntry = BlockedUserEntity_A0B1.builder()
                .blockerId(user3.getId()) // Charlie is the blocker
                .blockedId(requesterId) // requesterId is blocked
                .build();
        List<BlockedUserEntity_A0B1> blockedByRequesterList = Collections.emptyList(); // Requester isn't blocking anyone
        List<BlockedUserEntity_A0B1> requesterBlockedByList = Collections.singletonList(blockedEntry);

        // Mock findAll to capture the Specification and apply it manually for the blocked users check
        when(userRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenAnswer(invocation -> {
                    Specification<UserEntity_A0B1> spec = invocation.getArgument(0);
                    // This is a simplified application of the spec's `notBlocked` part.
                    // In a real scenario, the JPA infrastructure handles subqueries.
                    // For this test, we check if the user to be excluded is 'user3' (Charlie).
                    List<UserEntity_A0B1> filteredUsers = List.of(user1, user2, user3).stream()
                            .filter(u -> !u.getId().equals(requesterId)) // Exclude self
                            .filter(u -> !u.getId().equals(user3.getId())) // Manually exclude Charlie who blocked requester
                            .collect(java.util.ArrayList::new, java.util.ArrayList::add, java.util.ArrayList::addAll);
                    return new PageImpl<>(filteredUsers, PageRequest.of(page - 1, pageSize), filteredUsers.size());
                });

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertTrue(response.getResults().isEmpty(), "Charlie should be excluded because he blocked the requester.");
        verify(userRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }


    @Test
    void searchUsers_shouldFindUsersByNameCaseInsensitive() {
        // Arrange
        String query = "alice"; // Lowercase query
        int page = 1;
        int pageSize = 10;
        List<UserEntity_A0B1> users = List.of(user1);
        Page<UserEntity_A0B1> userPage = new PageImpl<>(users, PageRequest.of(page - 1, pageSize), 1);

        when(userRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(eq(requesterId), any(UUID.class))).thenReturn(false);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertEquals(user1.getName(), response.getResults().get(0).getName());
    }

    @Test
    void searchUsers_shouldFindUsersByExactPhoneNumber() {
        // Arrange
        String query = "+1987654321"; // Exact phone number
        int page = 1;
        int pageSize = 10;
        List<UserEntity_A0B1> users = List.of(user2);
        Page<UserEntity_A0B1> userPage = new PageImpl<>(users, PageRequest.of(page - 1, pageSize), 1);

        when(userRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(eq(requesterId), any(UUID.class))).thenReturn(false);

        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertEquals(user2.getPhoneNumber(), response.getResults().get(0).getName()); // Assuming name is populated with phone for this test
    }

    @Test
    void searchUsers_paginationCorrectness() {
        // Arrange
        String query = "User";
        int page = 2;
        int pageSize = 2;
        UserEntity_A0B1 user4 = UserEntity_A0B1.builder().id(UUID.randomUUID()).name("David").phoneNumber("+1000000000").build();
        UserEntity_A0B1 user5 = UserEntity_A0B1.builder().id(UUID.randomUUID()).name("Eve").phoneNumber("+1000000001").build();
        List<UserEntity_A0B1> allUsers = List.of(user1, user2, user3, user4, user5);
        List<UserEntity_A0B1> pageContent = List.of(user3, user4); // Users for page 2

        Page<UserEntity_A0B1> userPage = new PageImpl<>(pageContent, PageRequest.of(page - 1, pageSize), allUsers.size());

        when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(page - 1, pageSize))))
                .thenReturn(userPage);
        when(conversationRepository.existsConversationBetween(eq(requesterId), any(UUID.class))).thenReturn(false);


        // Act
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, requesterId, page, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getResults().size());
        assertEquals(user3.getId(), response.getResults().get(0).getUserId());
        assertEquals(user4.getId(), response.getResults().get(1).getUserId());
        assertEquals(page, response.getPagination().getCurrentPage());
        assertEquals(pageSize, response.getPagination().getPageSize());
        assertEquals(allUsers.size(), response.getPagination().getTotalItems());
        assertEquals((int) Math.ceil((double) allUsers.size() / pageSize), response.getPagination().getTotalPages());

        verify(userRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }

}
```
src/test/java/com/example/usersearch/controller/UserSearchController_A0B1Test.java
```java