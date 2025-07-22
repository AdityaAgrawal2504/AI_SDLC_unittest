package com.example.repository;

import com.example.entity.User_F4B8;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepository_F4B8Test {

    @Autowired
    private UserRepository_F4B8 userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User_F4B8 testUser;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        entityManager.clear();
        entityManager.flush(); // Ensure changes are written before deletion
        userRepository.deleteAll(); // Clear the H2 in-memory database
        
        testUser = new User_F4B8();
        testUser.setPhoneNumber("1234567890");
        testUser.setPasswordHash("hashedPassword");
        // ID, createdAt, updatedAt are handled by @GeneratedValue and @CreationTimestamp/@UpdateTimestamp
        // No need to set them explicitly for new entities
    }

    @Test
    void testSaveUser() {
        User_F4B8 savedUser = userRepository.save(testUser);
        assertNotNull(savedUser.getId());
        assertEquals("1234567890", savedUser.getPhoneNumber());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        Optional<User_F4B8> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser, foundUser.get());
    }

    @Test
    void testFindByPhoneNumber_Found() {
        entityManager.persist(testUser); // Use entityManager to persist without using repository, to test findByPhoneNumber
        entityManager.flush();

        Optional<User_F4B8> found = userRepository.findByPhoneNumber("1234567890");
        assertTrue(found.isPresent(), "User should be found by phone number");
        assertEquals(testUser.getPhoneNumber(), found.get().getPhoneNumber());
    }

    @Test
    void testFindByPhoneNumber_NotFound() {
        Optional<User_F4B8> found = userRepository.findByPhoneNumber("9999999999");
        assertFalse(found.isPresent(), "User should not be found for a non-existent phone number");
    }

    @Test
    void testUpdateUser() {
        User_F4B8 savedUser = entityManager.persistAndFlush(testUser); // Persist and flush immediately
        String newPasswordHash = "newHashedPassword";
        savedUser.setPasswordHash(newPasswordHash);
        
        User_F4B8 updatedUser = userRepository.save(savedUser); // Update using repository

        assertEquals(newPasswordHash, updatedUser.getPasswordHash());
        assertNotNull(updatedUser.getUpdatedAt()); // Check updated timestamp
        assertTrue(updatedUser.getUpdatedAt().isAfter(updatedUser.getCreatedAt())); // Updated after creation
    }

    @Test
    void testDeleteUser() {
        User_F4B8 savedUser = entityManager.persistAndFlush(testUser);
        UUID userId = savedUser.getId();

        userRepository.deleteById(userId);

        Optional<User_F4B8> found = userRepository.findById(userId);
        assertFalse(found.isPresent(), "User should be deleted");
    }

    @Test
    void testFindAllUsers() {
        User_F4B8 user1 = new User_F4B8();
        user1.setPhoneNumber("1111111111");
        user1.setPasswordHash("h1");
        entityManager.persist(user1);

        User_F4B8 user2 = new User_F4B8();
        user2.setPhoneNumber("2222222222");
        user2.setPasswordHash("h2");
        entityManager.persist(user2);
        entityManager.flush();

        long count = userRepository.count();
        assertEquals(2, count, "Should find two users");
    }
}
```
```java
src/test/java/com/example/exception/ErrorCode_F4B8Test.java