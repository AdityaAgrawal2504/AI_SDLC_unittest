package com.example.userregistration.service;

import com.example.userregistration.dto.UserRegistrationRequest_URAPI_1;
import com.example.userregistration.dto.UserRegistrationResponse_URAPI_1;
import com.example.userregistration.exception.PasswordHashingException_URAPI_1;
import com.example.userregistration.exception.UserAlreadyExistsException_URAPI_1;

/**
 * Interface defining the contract for user registration business logic.
 */
public interface IUserRegistrationService_URAPI_1 {
    /**
     * Handles the business logic for registering a new user.
     * @param request The user registration request data.
     * @return A response DTO containing the new user's ID.
     * @throws UserAlreadyExistsException_URAPI_1 if the phone number is already registered.
     * @throws PasswordHashingException_URAPI_1 if the password cannot be securely hashed.
     */
    UserRegistrationResponse_URAPI_1 registerUser(UserRegistrationRequest_URAPI_1 request);
}
```
src/main/java/com/example/userregistration/service/UserRegistrationService_URAPI_1.java
```java