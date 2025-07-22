package com.example.auth.repository;

import com.example.auth.model.User_LCVAPI_109;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for User entities.
 */
@Repository
public interface UserRepository_LCVAPI_110 extends JpaRepository<User_LCVAPI_109, UUID> {

    /**
     * Finds a user by their unique phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User_LCVAPI_109> findByPhoneNumber(String phoneNumber);
}
src/main/java/com/example/auth/service/AuthenticationService_LCVAPI_118.java