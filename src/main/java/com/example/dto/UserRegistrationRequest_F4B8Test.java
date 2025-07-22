package com.example.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationRequest_F4B8Test {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8();
        assertNotNull(request);
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        String phoneNumber = "1234567890";
        String password = "securePassword123";
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8(phoneNumber, password);

        assertEquals(phoneNumber, request.getPhoneNumber());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testSetters() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8();
        String phoneNumber = "0987654321";
        String password = "anotherSecurePassword";

        request.setPhoneNumber(phoneNumber);
        request.setPassword(password);

        assertEquals(phoneNumber, request.getPhoneNumber());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testValidRequest() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "password123");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid request should have no violations");
    }

    @Test
    void testInvalidPhoneNumber_blank() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("", "password123");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number must be provided.", violations.iterator().next().getMessage());
        assertEquals("phoneNumber", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testInvalidPhoneNumber_null() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8(null, "password123");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number must be provided.", violations.iterator().next().getMessage());
        assertEquals("phoneNumber", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testInvalidPhoneNumber_tooShort() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("12345", "password123");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number must be exactly 10 digits.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPhoneNumber_nonDigits() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("123A567890", "password123");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number must be exactly 10 digits.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPassword_blank() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size()); // @NotBlank and @Size will both fail
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password must be provided.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password must be at least 8 characters long.")));
        assertEquals("password", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testInvalidPassword_tooShort() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "short");
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPassword_null() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", null);
        Set<ConstraintViolation<UserRegistrationRequest_F4B8>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password must be provided.", violations.iterator().next().getMessage());
    }

    @Test
    void testToString() {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "password123");
        String expectedToString = "UserRegistrationRequest_F4B8(phoneNumber=1234567890, password=password123)";
        assertEquals(expectedToString, request.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        UserRegistrationRequest_F4B8 req1 = new UserRegistrationRequest_F4B8("1234567890", "password123");
        UserRegistrationRequest_F4B8 req2 = new UserRegistrationRequest_F4B8("1234567890", "password123");
        UserRegistrationRequest_F4B8 req3 = new UserRegistrationRequest_F4B8("9999999999", "newpassword");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotEquals(req1, req3);
        assertNotEquals(req1.hashCode(), req3.hashCode());
    }
}
```
```java
src/test/java/com/example/dto/UserRegistrationResponse_F4B8Test.java