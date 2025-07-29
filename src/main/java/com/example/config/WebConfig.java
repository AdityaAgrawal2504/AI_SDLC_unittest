package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General web configuration for the application.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final ApiLoggingFilter apiLoggingFilter;

    /**
     * Registers the ApiLoggingFilter to ensure it runs for every request.
     * @return The FilterRegistrationBean for the ApiLoggingFilter.
     */
    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> loggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiLoggingFilter);
        registrationBean.addUrlPatterns("/*");
        // Ensure this filter runs before the Spring Security filter chain
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }
}