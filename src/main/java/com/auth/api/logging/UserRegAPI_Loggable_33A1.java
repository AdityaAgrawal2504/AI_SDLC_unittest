package com.auth.api.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable automatic logging of method execution time.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRegAPI_Loggable_33A1 {
}
```
```java
// src/main/java/com/auth/api/logging/UserRegAPI_LoggingAspect_33A1.java