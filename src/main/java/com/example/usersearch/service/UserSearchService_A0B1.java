package com.example.usersearch.service;

import com.example.usersearch.dto.PaginationInfo_A0B1;
import com.example.usersearch.dto.UserSearchResponse_A0B1;
import com.example.usersearch.dto.UserSummary_A0B1;
import com.example.usersearch.exception.DatabaseSearchException_A0B1;
import com.example.usersearch.logging.Loggable_A0B1;
import com.example.usersearch.model.UserEntity_A0B1;
import com.example.usersearch.repository.ConversationRepository_A0B1;
import com.example.usersearch.repository.UserRepository_A0B1;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service providing the core business logic for searching users.
 */
@Service
@RequiredArgsConstructor
public class UserSearchService_A0B1 {

    private final UserRepository_A0B1 userRepository;
    private final ConversationRepository_A0B1 conversationRepository;

    /**
     * Searches for users based on a query, excluding the requester and users who have blocked the requester.
     * @param query The search term (name or phone number).
     * @param requesterId The UUID of the user performing the search.
     * @param page The 1-indexed page number.
     * @param pageSize The number of results per page.
     * @return A paginated response of user summaries.
     */
    @Loggable_A0B1
    @Transactional(readOnly = true)
    public UserSearchResponse_A0B1 searchUsers(String query, UUID requesterId, int page, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Specification<UserEntity_A0B1> spec = buildSpecification(query, requesterId);

            Page<UserEntity_A0B1> userPage = userRepository.findAll(spec, pageable);

            List<UserSummary_A0B1> results = userPage.getContent().stream()
                .map(user -> mapToUserSummary(user, requesterId))
                .collect(Collectors.toList());

            PaginationInfo_A0B1 paginationInfo = PaginationInfo_A0B1.builder()
                .currentPage(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .totalItems(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .build();

            return new UserSearchResponse_A0B1(paginationInfo, results);
        } catch (Exception e) {
            throw new DatabaseSearchException_A0B1("Failed to execute user search.", e);
        }
    }

    /**
     * Builds a JPA Specification for dynamic query construction.
     * @param query The search term.
     * @param requesterId The ID of the user to exclude from results.
     * @return A Specification object for the user search.
     */
    private Specification<UserEntity_A0B1> buildSpecification(String query, UUID requesterId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            // Predicate for searching by name (case-insensitive) or phone number
            Predicate nameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query.toLowerCase() + "%");
            Predicate phoneMatch = criteriaBuilder.equal(root.get("phoneNumber"), query);
            Predicate searchPredicate = criteriaBuilder.or(nameMatch, phoneMatch);

            // Predicate to exclude the current user
            Predicate excludeSelf = criteriaBuilder.notEqual(root.get("id"), requesterId);

            // Subquery to find IDs of users who have blocked the requester
            var subquery = criteriaQuery.subquery(UUID.class);
            var subqueryRoot = subquery.from(com.example.usersearch.model.BlockedUserEntity_A0B1.class);
            subquery.select(subqueryRoot.get("blockerId"))
                .where(criteriaBuilder.equal(subqueryRoot.get("blockedId"), requesterId));

            // Predicate to exclude users found in the subquery
            Predicate notBlocked = root.get("id").in(subquery).not();

            return criteriaBuilder.and(searchPredicate, excludeSelf, notBlocked);
        };
    }

    /**
     * Maps a UserEntity to a UserSummary DTO and checks for existing conversations.
     * @param user The UserEntity to map.
     * @param requesterId The ID of the searching user.
     * @return A UserSummary DTO.
     */
    private UserSummary_A0B1 mapToUserSummary(UserEntity_A0B1 user, UUID requesterId) {
        boolean hasConversation = conversationRepository.existsConversationBetween(requesterId, user.getId());

        return UserSummary_A0B1.builder()
            .userId(user.getId())
            .name(user.getName())
            .profilePictureUrl(user.getProfilePictureUrl())
            .hasExistingConversation(hasConversation)
            .build();
    }
}
```
src/main/java/com/example/usersearch/controller/UserSearchController_A0B1.java
```java