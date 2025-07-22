package com.example.usersearch.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark methods for automatic start/end logging via AOP.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable_A0B1 {
}
```
src/main/java/com/example/usersearch/logging/LoggerService_A0B1.java
```java