package com.example.grpc;

import io.grpc.Context;
import org.springframework.security.core.Authentication;

/**
 * Defines context keys for storing authentication information in the gRPC context.
 */
public final class GrpcAuthContext_GRPC_4002 {
    
    private GrpcAuthContext_GRPC_4002() {}

    public static final Context.Key<Authentication> AUTHENTICATION_KEY = Context.key("authentication");
}
```
```java
//
// Filename: src/main/java/com/example/grpc/ChatServiceImpl_GRPC_4003.java
//