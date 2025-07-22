package com.example.util;

/**
 * Utility class for managing a unique request ID per thread.
 * This ID can be used for logging and tracking request flows.
 */
public class RequestIdUtilLCVAPI_1 {

    private static final ThreadLocal<String> REQUEST_ID_HOLDER = new ThreadLocal<>();

    /**
     * Sets the request ID for the current thread.
     * @param requestId The unique ID to set.
     */
    public static void setRequestId(String requestId) {
        REQUEST_ID_HOLDER.set(requestId);
    }

    /**
     * Retrieves the request ID for the current thread.
     * @return The request ID, or "undefined" if not set.
     */
    public static String getRequestId() {
        String requestId = REQUEST_ID_HOLDER.get();
        return (requestId != null) ? requestId : "undefined";
    }

    /**
     * Clears the request ID from the current thread's ThreadLocal.
     * Should be called at the end of a request's lifecycle.
     */
    public static void clear() {
        REQUEST_ID_HOLDER.remove();
    }
}
```

src/main/java/com/example/util/RequestIdFilterLCVAPI_1.java
```java