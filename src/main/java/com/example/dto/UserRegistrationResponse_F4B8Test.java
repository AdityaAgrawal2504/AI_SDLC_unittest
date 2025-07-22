package com.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationResponse_F4B8Test {

    @Test
    void testNoArgsConstructor() {
        UserRegistrationResponse_F4B8 response = new UserRegistrationResponse_F4B8();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        String userId = "some-uuid-123";
        String message = "User registered successfully.";
        UserRegistrationResponse_F4B8 response = new UserRegistrationResponse_F4B8(userId, message);

        assertEquals(userId, response.getUserId());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetters() {
        UserRegistrationResponse_F4B8 response = new UserRegistrationResponse_F4B8();
        String userId = "another-uuid-456";
        String message = "User creation failed.";

        response.setUserId(userId);
        response.setMessage(message);

        assertEquals(userId, response.getUserId());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testToString() {
        String userId = "test-user-id";
        String message = "Hello from test";
        UserRegistrationResponse_F4B8 response = new UserRegistrationResponse_F4B8(userId, message);
        String expectedToString = "UserRegistrationResponse_F4B8(userId=" + userId + ", message=" + message + ")";
        assertEquals(expectedToString, response.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        UserRegistrationResponse_F4B8 res1 = new UserRegistrationResponse_F4B8("id1", "msg1");
        UserRegistrationResponse_F4B8 res2 = new UserRegistrationResponse_F4B8("id1", "msg1");
        UserRegistrationResponse_F4B8 res3 = new UserRegistrationResponse_F4B8("id2", "msg2");

        assertEquals(res1, res2);
        assertEquals(res1.hashCode(), res2.hashCode());
        assertNotEquals(res1, res3);
        assertNotEquals(res1.hashCode(), res3.hashCode()); // Not strictly required, but generally expected for Lombok's @Data
    }
}
```
```java
src/test/java/com/example/dto/ApiError_F4B8Test.java