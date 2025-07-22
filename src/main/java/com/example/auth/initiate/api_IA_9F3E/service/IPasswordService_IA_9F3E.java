package com.example.auth.initiate.api_IA_9F3E.service;

/**
 * Service contract for handling password hashing and verification.
 */
public interface IPasswordService_IA_9F3E {

    /**
     * Compares a plaintext password against a stored hash securely.
     * @param plainTextPassword The raw password provided by the user.
     * @param hashedPassword The hashed password stored in the database.
     * @return True if the passwords match, false otherwise.
     */
    boolean verifyPassword(String plainTextPassword, String hashedPassword);
}
```
```java