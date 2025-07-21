package com.yourorg.userregistration.service;

import com.yourorg.userregistration.dto.UserRegistrationRequestURAPI_1201;
import com.yourorg.userregistration.dto.UserRegistrationResponseURAPI_1201;

/**
 * Interface defining the contract for authentication-related services.
 */
public interface IAuthServiceURAPI_1201 {
    /**
     * Registers a new user based on the provided request data.
     * @param request The user registration request DTO.
     * @return A response DTO containing the new user's ID.
     */
    UserRegistrationResponseURAPI_1201 register(UserRegistrationRequestURAPI_1201 request);
}
```
```java
// src/main/java/com/yourorg/userregistration/service/AuthServiceURAPI_1201.java