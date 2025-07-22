package com.example.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiError_F4B8Test {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testNoArgsConstructor() {
        ApiError_F4B8 error = new ApiError_F4B8();
        assertNotNull(error);
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        String errorCode = "TEST_CODE";
        String message = "Test message";
        Map<String, String> details = new HashMap<>();
        details.put("field", "error");

        ApiError_F4B8 error = new ApiError_F4B8(errorCode, message, details);

        assertEquals(errorCode, error.getErrorCode());
        assertEquals(message, error.getMessage());
        assertEquals(details, error.getDetails());
    }

    @Test
    void testTwoArgsConstructorAndGetters() {
        String errorCode = "ANOTHER_CODE";
        String message = "Another message";

        ApiError_F4B8 error = new ApiError_F4B8(errorCode, message);

        assertEquals(errorCode, error.getErrorCode());
        assertEquals(message, error.getMessage());
        assertNull(error.getDetails()); // Should be null with this constructor
    }

    @Test
    void testSetters() {
        ApiError_F4B8 error = new ApiError_F4B8();
        String errorCode = "SET_CODE";
        String message = "Set message";
        Map<String, String> details = Collections.singletonMap("key", "value");

        error.setErrorCode(errorCode);
        error.setMessage(message);
        error.setDetails(details);

        assertEquals(errorCode, error.getErrorCode());
        assertEquals(message, error.getMessage());
        assertEquals(details, error.getDetails());
    }

    @Test
    void testJsonIncludeNonNull() throws JsonProcessingException {
        ApiError_F4B8 errorWithDetails = new ApiError_F4B8("CODE_1", "Message 1", Collections.singletonMap("f1", "e1"));
        String jsonWithDetails = objectMapper.writeValueAsString(errorWithDetails);
        assertTrue(jsonWithDetails.contains("\"details\":"));

        ApiError_F4B8 errorWithoutDetails = new ApiError_F4B8("CODE_2", "Message 2");
        String jsonWithoutDetails = objectMapper.writeValueAsString(errorWithoutDetails);
        assertFalse(jsonWithoutDetails.contains("\"details\":"));
    }

    @Test
    void testToString() {
        ApiError_F4B8 error = new ApiError_F4B8("CODE", "Msg");
        assertTrue(error.toString().contains("errorCode=CODE"));
        assertTrue(error.toString().contains("message=Msg"));
    }

    @Test
    void testEqualsAndHashCode() {
        ApiError_F4B8 err1 = new ApiError_F4B8("C1", "M1", Collections.singletonMap("k","v"));
        ApiError_F4B8 err2 = new ApiError_F4B8("C1", "M1", Collections.singletonMap("k","v"));
        ApiError_F4B8 err3 = new ApiError_F4B8("C2", "M2");

        assertEquals(err1, err2);
        assertEquals(err1.hashCode(), err2.hashCode());
        assertNotEquals(err1, err3);
        assertNotEquals(err1.hashCode(), err3.hashCode());
    }
}
```
```java
src/test/java/com/example/entity/User_F4B8Test.java