package com.yourorg.verifyotp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspectVAPI_1 {

    private static final Logger logger = LogManager.getLogger(LoggingAspectVAPI_1.class);

    /**
     * Defines a pointcut for all methods within classes annotated with @RestController or @Service.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Logs method entry, exit, execution time, and errors around the defined pointcut.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("Enter: {}.{}() with argument[s] = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Exit: {}.{}() with result = {}; Execution time = {} ms", className, methodName, result, timeTaken);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), className, methodName);
            throw e;
        } catch (Throwable t) {
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.error("Exception in {}.{}() with cause = '{}' and exception = '{}'. Execution time = {} ms",
                className, methodName, t.getCause() != null ? t.getCause() : "NULL", t.getMessage(), timeTaken);
            throw t;
        }
    }
}
```
```xml
<!-- resources: application.properties -->
# Server Configuration
server.port=8080

# Database Configuration (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:otpdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Custom Application Properties
otp.verification.max.attempts=3
jwt.access.token.expiry=3600

# Logging Configuration
logging.config=classpath:log4j2-VAPI.xml
```
```xml
<!-- resources: log4j2-VAPI.xml -->