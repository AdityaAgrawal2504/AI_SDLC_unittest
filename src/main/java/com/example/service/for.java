package com.example.service;

import com.example.dto.UserRegistrationRequest_F4B8;
import com.example.dto.UserRegistrationResponse_F4B8;
import com.example.exception.PasswordHashingException_F4B8;
import com.example.exception.UserAlreadyExistsException_F4B8;

/**
 * Service interface for user registration business logic.
 */
public interface IUserRegistrationService_F4B8 {

    /**
     * Handles the business logic for registering a new user.
     * @param request The user registration request DTO.
     * @return A response DTO containing the new user's ID and a success message.
     * @throws UserAlreadyExistsException_F4B8 if a user with the phone number already exists.
     * @throws PasswordHashingException_F4B8 if password hashing fails.
     */
    UserRegistrationResponse_F4B8 registerUser(UserRegistrationRequest_F4B8 request);
}
```
```java
src/main/java/com/example/service/impl/UserRegistrationServiceImpl_F4B8.java