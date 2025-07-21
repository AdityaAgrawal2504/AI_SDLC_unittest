package com.fetchconversations.api.config;

import com.fetchconversations.api.validation.StringToSortFieldFCA911Converter;
import com.fetchconversations.api.validation.StringToSortOrderFCA911Converter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures web components like custom converters.
 */
@Configuration
public class WebConfigFCA911 implements WebMvcConfigurer {

    /**
     * Registers custom converters for handling specific request parameters.
     * @param registry The registry to add converters to.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSortFieldFCA911Converter());
        registry.addConverter(new StringToSortOrderFCA911Converter());
    }
}
```
```java
// src/main/java/com/fetchconversations/api/config/SecurityConfigFCA911.java