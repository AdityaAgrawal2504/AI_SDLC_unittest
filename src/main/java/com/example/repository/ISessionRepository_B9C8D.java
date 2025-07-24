src/main/java/com/example/repository/ISessionRepository_B9C8D.java
package com.example.repository;

import com.example.model.Session_B1C2D;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISessionRepository_B9C8D extends JpaRepository<Session_B1C2D, UUID> {
    Optional<Session_B1C2D> findByRefreshTokenHash(String refreshTokenHash);
    
    @Transactional
    void deleteByUserId(UUID userId);
}