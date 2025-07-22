package com.example.util;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A servlet filter that generates a unique request ID for each incoming request.
 * It stores the ID in a ThreadLocal and Log4j's ThreadContext for logging purposes.
 */
public class RequestIdFilterLCVAPI_1 extends OncePerRequestFilter {

    private static final String REQUEST_ID_KEY = "requestId";

    /**
     * Processes each HTTP request to add a unique request ID.
     * @param request The incoming servlet request.
     * @param response The outgoing servlet response.
     * @param filterChain The filter chain to pass the request along.
     * @throws ServletException on servlet errors.
     * @throws IOException on I/O errors.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        RequestIdUtilLCVAPI_1.setRequestId(requestId);
        ThreadContext.put(REQUEST_ID_KEY, requestId);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestIdUtilLCVAPI_1.clear();
            ThreadContext.clearMap();
        }
    }
}
```

src/main/java/com/example/config/DataInitializerLCVAPI_1.java
```java