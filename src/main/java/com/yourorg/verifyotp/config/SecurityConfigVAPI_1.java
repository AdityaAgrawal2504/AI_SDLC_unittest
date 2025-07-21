package com.yourorg.verifyotp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfigVAPI_1 {

    /**
     * Provides a BCrypt password encoder bean for hashing and checking passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoderVAPI_1() {
        return new BCryptPasswordEncoder();
    }
}
```
```java
// Service Interface: ITokenServiceVAPI_1.java