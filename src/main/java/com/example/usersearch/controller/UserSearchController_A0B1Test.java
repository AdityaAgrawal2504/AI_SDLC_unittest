package com.example.usersearch.controller;

import com.example.usersearch.dto.ErrorResponse_A0B1;
import com.example.usersearch.dto.PaginationInfo_A0B1;
import com.example.usersearch.dto.UserSearchResponse_A0B1;
import com.example.usersearch.dto.UserSummary_A0B1;
import com.example.usersearch.enums.ErrorCode_A0B1;
import com.example.usersearch.exception.DatabaseSearchException_A0B1;
import com.example.usersearch.security.AuthenticatedUser_A0B1;
import com.example.usersearch.security.JwtRequestFilter_A0B1;
import com.example.usersearch.security.JwtUtil_A0B1;
import com.example.usersearch.security.UserDetailsServiceImpl_A0B1;
import com.example.usersearch.service.UserSearchService_A0B1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Exclude JwtRequestFilter from full scan to isolate controller testing
@WebMvcTest(controllers = UserSearchController_A0B1.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter_A0B1.class, JwtUtil_A0B1.class, UserDetailsServiceImpl_A0B1.class}))
class UserSearchController_A0B1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserSearchService_A0B1 userSearchService;

    // We need a mock authenticated user for @AuthenticationPrincipal
    private AuthenticatedUser_A0B1 mockAuthenticatedUser;
    private UUID requesterId;

    @BeforeEach
    void setUp() {
        requesterId = UUID.randomUUID();
        mockAuthenticatedUser = new AuthenticatedUser_A0B1(
                requesterId,
                "Test User",
                "+1234567890",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void searchUsers_shouldReturnOkAndResults_validQuery() throws Exception {
        // Arrange
        String query = "testuser";
        int page = 1;
        int pageSize = 10;

        UserSummary_A0B1 userSummary = UserSummary_A0B1.builder()
                .userId(UUID.randomUUID())
                .name("Test User 1")
                .profilePictureUrl("http://example.com/pfp1.jpg")
                .hasExistingConversation(false)
                .build();
        PaginationInfo_A0B1 paginationInfo = PaginationInfo_A0B1.builder()
                .currentPage(page)
                .pageSize(pageSize)
                .totalItems(1)
                .totalPages(1)
                .build();
        UserSearchResponse_A0B1 mockResponse = new UserSearchResponse_A0B1(paginationInfo, List.of(userSummary));

        when(userSearchService.searchUsers(eq(query), eq(requesterId), eq(page), eq(pageSize)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .with(user(mockAuthenticatedUser)) // Authenticate the request
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentPage").value(page))
                .andExpect(jsonPath("$.results[0].name").value("Test User 1"));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_missingQueryParam() throws Exception {
        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_QUERY_MISSING.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("q' query parameter is required"));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_queryTooShort() throws Exception {
        // Arrange
        String query = "ab"; // Too short

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_QUERY_TOO_SHORT.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("Search query must be between 3 and 100 characters."));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_queryTooLong() throws Exception {
        // Arrange
        String query = "a".repeat(101); // Too long

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_QUERY_TOO_LONG.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("Search query must be between 3 and 100 characters."));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_invalidPageNumber() throws Exception {
        // Arrange
        String query = "test";
        int page = 0; // Invalid page number

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .param("page", String.valueOf(page))
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_INVALID_PAGE_NUMBER.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("Page number must be a positive integer."));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_invalidPageSizeTooSmall() throws Exception {
        // Arrange
        String query = "test";
        int pageSize = 0; // Invalid page size

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .param("pageSize", String.valueOf(pageSize))
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_INVALID_PAGE_SIZE.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("Page size must be at least 1."));
    }

    @Test
    void searchUsers_shouldReturnBadRequest_invalidPageSizeTooLarge() throws Exception {
        // Arrange
        String query = "test";
        int pageSize = 101; // Invalid page size

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .param("pageSize", String.valueOf(pageSize))
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.VALIDATION_INVALID_PAGE_SIZE.getCode(), errorResponse.getErrorCode());
        assertTrue(errorResponse.getMessage().contains("Page size cannot be more than 100."));
    }


    @Test
    void searchUsers_shouldReturnInternalServerError_serviceThrowsException() throws Exception {
        // Arrange
        String query = "error";
        int page = 1;
        int pageSize = 10;

        when(userSearchService.searchUsers(eq(query), eq(requesterId), eq(page), eq(pageSize)))
                .thenThrow(new DatabaseSearchException_A0B1("Simulated DB error", new RuntimeException()));

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .with(user(mockAuthenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse_A0B1 errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse_A0B1.class);

        assertEquals(ErrorCode_A0B1.DATABASE_SEARCH_FAILED.getCode(), errorResponse.getErrorCode());
        assertEquals(ErrorCode_A0B1.DATABASE_SEARCH_FAILED.getMessage(), errorResponse.getMessage());
    }

    @Test
    void searchUsers_shouldReturnUnauthorized_noAuthentication() throws Exception {
        // Arrange
        String query = "test";

        // Act & Assert
        mockMvc.perform(get("/users/search")
                        .param("q", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // No .with(user()) provided
    }
}
```