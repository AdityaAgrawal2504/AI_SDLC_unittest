package com.auth.api.service.hasher;

/**
 * Interface for a password hashing service.
 */
public interface UserRegAPI_PasswordHasher_33A1 {
    /**
     * Hashes a plain text password.
     * @param plainTextPassword The password to hash.
     * @return The resulting hash as a string.
     */
    String hash(String plainTextPassword);
}
```
```java
// src/main/java/com/auth/api/service/hasher/UserRegAPI_BCryptPasswordHasher_33A1.java