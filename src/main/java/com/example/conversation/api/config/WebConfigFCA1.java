package com.example.conversation.api.config;

import com.example.conversation.api.enums.SortByFCA1;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures web-related components for the application.
 */
@Configuration
public class WebConfigFCA1 implements WebMvcConfigurer {

    /**
     * Adds custom converters to the Spring formatting service.
     *
     * @param registry The registry to add converters to.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortByFCA1.StringToSortByFCA1Converter());
    }
}
src/main/java/com/example/conversation/api/config/SecurityConfigFCA1.java