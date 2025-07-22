package com.example.usersearch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI (Swagger) documentation.
 * Defines the security scheme for Bearer Token authentication.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "User Search API",
        version = "1.0",
        description = "API for searching users within the application."
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Bearer token for authenticating the logged-in user."
)
public class OpenApiConfig_A0B1 {
}
```
src/main/java/com/example/usersearch/util/DataInitializer_A0B1.java
```java