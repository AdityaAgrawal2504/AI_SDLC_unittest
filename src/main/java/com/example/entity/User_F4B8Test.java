package com.example.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class User_F4B8Test {

    @Test
    void testUserEntityCreationAndGettersSetters() {
        User_F4B8 user = new User_F4B8();
        UUID id = UUID.randomUUID();
        String phoneNumber = "1234567890";
        String passwordHash = "hashedPassword";
        Instant now = Instant.now();

        user.setId(id);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(passwordHash);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(id, user.getId());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID(); // Different ID

        User_F4B8 user1 = new User_F4B8();
        user1.setId(id1);
        user1.setPhoneNumber("1111111111");
        user1.setPasswordHash("hash1");

        User_F4B8 user2 = new User_F4B8();
        user2.setId(id1); // Same ID
        user2.setPhoneNumber("1111111111"); // Same phone number
        user2.setPasswordHash("hash_diff"); // Different password hash, should still be equal based on ID/phoneNumber

        User_F4B8 user3 = new User_F4B8();
        user3.setId(id2); // Different ID
        user3.setPhoneNumber("2222222222"); // Different phone number
        user3.setPasswordHash("hash2");

        User_F4B8 user4 = new User_F4B8();
        user4.setId(id1); // Same ID
        user4.setPhoneNumber("9999999999"); // Different phone number

        // Test equality based on id and phoneNumber
        assertEquals(user1, user2, "Users with same ID and phone number should be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal for equal objects");

        assertNotEquals(user1, user3, "Users with different IDs and phone numbers should not be equal");
        assertNotEquals(user1.hashCode(), user3.hashCode(), "Hash codes should not be equal for different objects");

        assertNotEquals(user1, user4, "Users with same ID but different phone numbers should not be equal");
        assertNotEquals(user1.hashCode(), user4.hashCode(), "Hash codes should not be equal if relevant fields differ");
    }

    @Test
    void testToStringExcludesPasswordHash() {
        User_F4B8 user = new User_F4B8();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber("1234567890");
        user.setPasswordHash("highlySensitiveHash");

        String toStringResult = user.toString();
        assertTrue(toStringResult.contains(user.getId().toString()), "ToString should contain ID");
        assertTrue(toStringResult.contains(user.getPhoneNumber()), "ToString should contain phone number");
        assertFalse(toStringResult.contains("highlySensitiveHash"), "ToString should NOT contain password hash");
        assertFalse(toStringResult.contains("passwordHash"), "ToString should NOT contain passwordHash field name");
    }
}
```
```java
src/test/java/com/example/repository/UserRepository_F4B8Test.java