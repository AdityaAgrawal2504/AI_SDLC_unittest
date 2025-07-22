package com.example.auth.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Utility class to manage the lifecycle of a request ID using ThreadLocal and SLF4J's MDC.
 */
public final class RequestIdUtil_LCVAPI_111 {

    private static final String REQUEST_ID_KEY = "requestId";
    private static final ThreadLocal<String> requestId = new ThreadLocal<>();

    /**
     * Generates and sets a new unique request ID.
     */
    public static void generateAndSet() {
        String id = UUID.randomUUID().toString();
        requestId.set(id);
        MDC.put(REQUEST_ID_KEY, id);
    }

    /**
     * Retrieves the current request ID.
     * @return The unique ID for the current request.
     */
    public static String get() {
        return requestId.get();
    }

    /**
     * Clears the request ID from ThreadLocal and MDC.
     */
    public static void clear() {
        requestId.remove();
        MDC.remove(REQUEST_ID_KEY);
    }
}
src/main/resources/application.properties
server.port=8080
spring.application.name=LoginCredentialVerificationAPI

# In-memory H2 Database settings for demonstration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:authdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Custom Properties
# Maximum number of failed login attempts before locking an account.
app.security.max-failed-attempts=5
src/main/resources/log4j2.xml