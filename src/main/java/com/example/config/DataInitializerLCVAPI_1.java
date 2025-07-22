package com.example.config;

import com.example.api.auth.domain.UserLCVAPI_1;
import com.example.api.auth.domain.UserStatusLCVAPI_1;
import com.example.api.auth.repository.UserRepositoryLCVAPI_1;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializerLCVAPI_1 implements CommandLineRunner {

    private final UserRepositoryLCVAPI_1 userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes after the application context is loaded to populate initial data.
     * @param args Command line arguments.
     * @throws Exception on failure.
     */
    @Override
    public void run(String... args) throws Exception {
        // Create an active user
        if (userRepository.findByPhoneNumber("+14155552671").isEmpty()) {
            UserLCVAPI_1 activeUser = UserLCVAPI_1.builder()
                .phoneNumber("+14155552671")
                .passwordHash(passwordEncoder.encode("MyS3cur3P@ssw0rd!"))
                .status(UserStatusLCVAPI_1.ACTIVE)
                .failedLoginAttempts(0)
                .build();
            userRepository.save(activeUser);
        }
        
        // Create a locked user
        if (userRepository.findByPhoneNumber("+14155552672").isEmpty()) {
            UserLCVAPI_1 lockedUser = UserLCVAPI_1.builder()
                .phoneNumber("+14155552672")
                .passwordHash(passwordEncoder.encode("LockedUserPass123"))
                .status(UserStatusLCVAPI_1.LOCKED)
                .failedLoginAttempts(5)
                .build();
            userRepository.save(lockedUser);
        }
        
        // Create an inactive user
        if (userRepository.findByPhoneNumber("+14155552673").isEmpty()) {
            UserLCVAPI_1 inactiveUser = UserLCVAPI_1.builder()
                .phoneNumber("+14155552673")
                .passwordHash(passwordEncoder.encode("InactiveUserPass123"))
                .status(UserStatusLCVAPI_1.INACTIVE)
                .failedLoginAttempts(0)
                .build();
            userRepository.save(inactiveUser);
        }
    }
}
```

src/main/java/com/example/logging/ExternalLogAppenderLCVAPI_1.java
```java