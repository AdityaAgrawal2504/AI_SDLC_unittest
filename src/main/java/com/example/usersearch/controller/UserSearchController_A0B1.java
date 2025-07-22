package com.example.usersearch.controller;

import com.example.usersearch.dto.ErrorResponse_A0B1;
import com.example.usersearch.dto.UserSearchResponse_A0B1;
import com.example.usersearch.security.AuthenticatedUser_A0B1;
import com.example.usersearch.service.UserSearchService_A0B1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling user search requests.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Endpoints for user management and search")
public class UserSearchController_A0B1 {

    private final UserSearchService_A0B1 userSearchService;

    /**
     * Handles GET requests to search for users by name or phone number.
     * @param query The search query string.
     * @param page The 1-indexed page number for pagination.
     * @param pageSize The number of results to return per page.
     * @param currentUser The authenticated user performing the search.
     * @return A ResponseEntity containing the paginated search results.
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search for users by name or phone number",
        description = "Allows a logged-in user to search for other users by phone number or name to initiate a new conversation. Results exclude the current user and users who have blocked the requester.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSearchResponse_A0B1.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse_A0B1.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse_A0B1.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse_A0B1.class)))
    })
    public ResponseEntity<UserSearchResponse_A0B1> searchUsers(
        @Parameter(description = "The search query, which can be a partial name or a full phone number in E.164 format.", required = true)
        @RequestParam(name = "q")
        @NotBlank(message = "The 'q' query parameter is required.")
        @Size(min = 3, max = 100, message = "Search query must be between 3 and 100 characters.")
        String query,

        @Parameter(description = "The page number for pagination, 1-indexed.")
        @RequestParam(name = "page", defaultValue = "1")
        @Min(value = 1, message = "Page number must be a positive integer.")
        int page,

        @Parameter(description = "The number of results to return per page.")
        @RequestParam(name = "pageSize", defaultValue = "20")
        @Min(value = 1, message = "Page size must be at least 1.")
        @Max(value = 100, message = "Page size cannot be more than 100.")
        int pageSize,

        @AuthenticationPrincipal AuthenticatedUser_A0B1 currentUser
    ) {
        UserSearchResponse_A0B1 response = userSearchService.searchUsers(query, currentUser.getId(), page, pageSize);
        return ResponseEntity.ok(response);
    }
}
```
src/main/java/com/example/usersearch/exception/GlobalExceptionHandler_A0B1.java
```java