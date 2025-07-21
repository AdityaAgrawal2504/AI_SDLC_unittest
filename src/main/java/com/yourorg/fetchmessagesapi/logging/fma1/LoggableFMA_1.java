package com.yourorg.fetchmessagesapi.logging.fma1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods for automatic structured logging.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableFMA_1 {
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/logging/fma1/LoggingAspectFMA_1.java