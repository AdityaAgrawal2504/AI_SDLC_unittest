package com.example.chat.v1.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to enable automatic structured logging for method execution.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableC1V1 {
}
```
```java
// src/main/java/com/example/chat/v1/logging/AbstractLoggerC1V1.java