package com.example.messagingapp.repository;

import com.example.messagingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Searches for users whose phone number contains the given query string.
     * @param query The partial phone number to search for.
     * @return A list of matching users.
     */
    List<User> findByPhoneNumberContaining(String query);
}