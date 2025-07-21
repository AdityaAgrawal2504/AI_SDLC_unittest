package com.yourorg.fetchconversationsapi.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * An AOP Aspect for structured logging of method execution.
 * It logs the entry, exit, and execution time of methods in controllers and services.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspectFCA8123 {

    private final LoggerServiceFCA8123 logger;

    /**
     * Logs method execution details around methods in the controller and service packages.
     * @param joinPoint The pointcut where the advice is applied.
     * @return The result of the target method execution.
     * @throws Throwable if the target method throws an exception.
     */
    @Around("execution(* com.yourorg.fetchconversationsapi.controller..*.*(..)) || execution(* com.yourorg.fetchconversationsapi.service..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> arguments = getMethodArguments(joinPoint);
        logger.info("Enter: {}.{}() with arguments: {}", className, methodName, arguments);

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.error("Exception in {}.{}() after {} ms", throwable, className, methodName, executionTime);
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("Exit: {}.{}() with result: [TRUNCATED] in {} ms", className, methodName, executionTime);

        return result;
    }

    private Map<String, Object> getMethodArguments(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> arguments = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            arguments.put(parameterNames[i], args[i] != null ? args[i].toString() : "null");
        }
        return arguments;
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/service/ConversationServiceFCA8123.java
<ctrl62>
public interface ConversationServiceFCA8123 {

    /**
     * Retrieves a paginated list of conversations based on specified criteria.
     *
     * @param query The search term to filter conversations.
     * @param sort The field to sort by.
     * @param order The sorting order.
     * @param page The page number.
     * @param pageSize The number of items per page.
     * @return A paginated response containing the list of conversations and pagination info.
     */
    PaginatedConversationsResponseFCA8123 fetchConversations(
        String query,
        ConversationSortFieldFCA8123 sort,
        SortOrderFCA8123 order,
        int page,
        int pageSize
    );
}
```
src/main/java/com/yourorg/fetchconversationsapi/service/ConversationServiceImplFCA8123.java
<ctrl62>build();
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/controller/ConversationControllerFCA8123.java
```java