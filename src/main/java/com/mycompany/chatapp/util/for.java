package com.mycompany.chatapp.util;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/**
 * Utility class for creating standard gRPC error responses.
 */
public final class GrpcErrorUtil_RealTimeChatStreamAPI_1122 {

    private GrpcErrorUtil_RealTimeChatStreamAPI_1122() {}

    public static StatusRuntimeException unauthenticated(String description) {
        return Status.UNAUTHENTICATED.withDescription(description).asRuntimeException();
    }

    public static StatusRuntimeException invalidArgument(String description) {
        return Status.INVALID_ARGUMENT.withDescription(description).asRuntimeException();
    }

    public static StatusRuntimeException notFound(String description) {
        return Status.NOT_FOUND.withDescription(description).asRuntimeException();
    }

    public static StatusRuntimeException permissionDenied(String description) {
        return Status.PERMISSION_DENIED.withDescription(description).asRuntimeException();
    }

    public static StatusRuntimeException failedPrecondition(String description) {
        return Status.FAILED_PRECONDITION.withDescription(description).asRuntimeException();
    }

     public static StatusRuntimeException resourceExhausted(String description) {
        return Status.RESOURCE_EXHAUSTED.withDescription(description).asRuntimeException();
    }

    public static StatusRuntimeException internal(String description) {
        return Status.INTERNAL.withDescription(description).asRuntimeException();
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/util/IdempotencyManager_RealTimeChatStreamAPI_1122.java