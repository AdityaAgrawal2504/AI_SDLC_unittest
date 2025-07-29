package com.example.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private ApiLoggingFilter apiLoggingFilter;

    @InjectMocks
    private WebConfig webConfig;

    @Test
    void loggingFilter_shouldRegisterApiLoggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registrationBean = webConfig.loggingFilter();

        assertEquals(apiLoggingFilter, registrationBean.getFilter());
        assertTrue(registrationBean.getUrlPatterns().contains("/*"));
        assertEquals(Integer.MIN_VALUE, registrationBean.getOrder());
    }
}