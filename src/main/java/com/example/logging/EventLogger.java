package com.example.logging;

import java.util.Map;

public interface EventLogger {
    void log(String eventName, Map<String, Object> details);
}