package com.yourorg.verifyotp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.yourorg.verifyotp.repository")
@EnableAspectJAutoProxy
public class VerifyOtpApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerifyOtpApiApplication.class, args);
    }

}
```
```java
// DTO: VerifyOtpRequestVAPI_1.java