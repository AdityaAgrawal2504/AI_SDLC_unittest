package com.auth.api.service;

import com.auth.api.dto.UserRegAPI_UserRegistrationRequest_33A1;
import com.auth.api.dto.UserRegAPI_UserRegistrationResponse_33A1;

/**
 * Interface defining the contract for the authentication service.
 */
public interface UserRegAPI_AuthService_33A1 {
    /**
     * Registers a new user based on the provided request data.
     * @param request The user registration request DTO.
     * @return A DTO containing the new user's ID and registration status.
     */
    UserRegAPI_UserRegistrationResponse_33A1 register(UserRegAPI_UserRegistrationRequest_33A1 request);
}
```
```java
// src/main/java/com/auth/api/service/UserRegAPI_AuthServiceImpl_33A1.java