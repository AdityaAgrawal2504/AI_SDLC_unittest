src/main/java/com/example/repository/IConversationRepository_S5T4U.java
package com.example.repository;

import com.example.model.Conversation_P3Q4R;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IConversationRepository_S5T4U extends JpaRepository<Conversation_P3Q4R, UUID> {
    
    @Query("SELECT c FROM Conversation_P3Q4R c JOIN c.participants p WHERE p.user.id = :userId ORDER BY c.updatedAt DESC")
    Page<Conversation_P3Q4R> findConversationsByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    @Query("SELECT c FROM Conversation_P3Q4R c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.user.id = :userId1 AND p2.user.id = :userId2 " +
           "GROUP BY c.id HAVING COUNT(c.id) = 2")
    Optional<Conversation_P3Q4R> findConversationBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}