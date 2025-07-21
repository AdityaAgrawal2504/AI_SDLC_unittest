package com.auth.api.constants;

/**
 * Holds constants for validation rules, such as regular expressions and messages.
 */
public final class UserRegAPI_ValidationConstants_33A1 {

    private UserRegAPI_ValidationConstants_33A1() {}

    public static final String PHONE_NUMBER_REGEX = "^\\+[1-9]\\d{1,14}$";
    public static final String PHONE_NUMBER_MESSAGE = "Phone number must be in valid E.164 format (e.g., +14155552671)";

    // Requires: 1 lowercase, 1 uppercase, 1 digit, 1 special char, min 8 chars
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,128}$";
    public static final String PASSWORD_MESSAGE = "Password must be 8-128 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character (!@#$%^&*)";
}
```
```java
// src/main/java/com/auth/api/enums/UserRegAPI_ErrorCode_33A1.java