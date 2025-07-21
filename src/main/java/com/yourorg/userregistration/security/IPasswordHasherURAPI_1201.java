package com.yourorg.userregistration.security;

/**
 * Interface for a password hashing service.
 */
public interface IPasswordHasherURAPI_1201 {

    /**
     * Hashes a plain-text password.
     * @param plainTextPassword The password to hash.
     * @return The hashed password string.
     */
    String hash(String plainTextPassword);
}
```
```java
// src/main/java/com/yourorg/userregistration/security/PasswordHasherURAPI_1201.java