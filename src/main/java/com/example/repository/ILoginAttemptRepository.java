package com.example.repository;

import com.example.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for LoginAttempt entities.
 */
@Repository
public interface ILoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {
}