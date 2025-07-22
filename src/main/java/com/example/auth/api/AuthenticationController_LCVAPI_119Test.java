package com.example.auth.api;

import com.example.auth.dto.LoginRequest_LCVAPI_104;
import com.example.auth.dto.LoginSuccessResponse_LCVAPI_105;
import com.example.auth.enums.ErrorCode_LCVAPI_107;
import com.example.auth.exception.CustomApiException_LCVAPI_114;
import com.example.auth.service.IAuthenticationService_LCVAPI_117;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController_LCVAPI_119.class)
// Needed to ensure GlobalExceptionHandler and RequestIdFilter are picked up by @WebMvcTest
@ContextConfiguration(classes = {AuthenticationController_LCVAPI_119.class, com.example.auth.exception.GlobalExceptionHandler_LCVAPI_115.class, com.example.auth.config.RequestIdFilter_LCVAPI_112.class})
class AuthenticationController_LCVAPI_119Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthenticationService_LCVAPI_117 authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    // Simulate RequestIdUtil's MDC behavior for tests
    private static final String MOCKED_REQUEST_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // Mock RequestIdUtil.get() to return a predictable value for test assertions
        Mockito.mockStatic(com.example.auth.util.RequestIdUtil_LCVAPI_111.class).when(com.example.auth.util.RequestIdUtil_LCVAPI_111::get).thenReturn(MOCKED_REQUEST_ID);
        Mockito.doNothing().when(com.example.auth.util.RequestIdUtil_LCVAPI_111.class);
    }

    @Test
    @DisplayName("Should return 200 OK and success message for valid login credentials")
    void login_ValidCredentials_ReturnsSuccess() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+14155552671", "MyS3cur3P@ssw0rd!");
        LoginSuccessResponse_LCVAPI_105 expectedResponse = new LoginSuccessResponse_LCVAPI_105("OTP sent successfully to the registered phone number.", MOCKED_REQUEST_ID);

        Mockito.when(authenticationService.loginWithCredentials(any(LoginRequest_LCVAPI_104.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent successfully to the registered phone number."))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid phone number format")
    void login_InvalidPhoneNumberFormat_ReturnsBadRequest() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("invalid_phone", "password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.INVALID_INPUT.name()))
                .andExpect(jsonPath("$.errorMessage").value("Phone number must be in E.164 format (e.g., +14155552671)."))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for blank password")
    void login_BlankPassword_ReturnsBadRequest() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+14155552671", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.INVALID_INPUT.name()))
                .andExpect(jsonPath("$.errorMessage").value("Password cannot be blank., Password must be between 8 and 128 characters."))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized for INVALID_CREDENTIALS exception")
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+14155552671", "wrong_password");

        Mockito.when(authenticationService.loginWithCredentials(any(LoginRequest_LCVAPI_104.class)))
                .thenThrow(new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS.name()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode_LCVAPI_107.INVALID_CREDENTIALS.getDefaultMessage()))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 403 Forbidden for ACCOUNT_LOCKED exception")
    void login_AccountLocked_ReturnsForbidden() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+15005550002", "password");

        Mockito.when(authenticationService.loginWithCredentials(any(LoginRequest_LCVAPI_104.class)))
                .thenThrow(new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.ACCOUNT_LOCKED));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.ACCOUNT_LOCKED.name()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode_LCVAPI_107.ACCOUNT_LOCKED.getDefaultMessage()))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 503 Service Unavailable for OTP_SERVICE_UNAVAILABLE exception")
    void login_OtpServiceUnavailable_ReturnsServiceUnavailable() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+14155552671", "MyS3cur3P@ssw0rd!");

        Mockito.when(authenticationService.loginWithCredentials(any(LoginRequest_LCVAPI_104.class)))
                .thenThrow(new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE.name()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE.getDefaultMessage()))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for unhandled exceptions")
    void login_UnhandledException_ReturnsInternalServerError() throws Exception {
        LoginRequest_LCVAPI_104 request = new LoginRequest_LCVAPI_104("+14155552671", "MyS3cur3P@ssw0rd!");

        Mockito.when(authenticationService.loginWithCredentials(any(LoginRequest_LCVAPI_104.class)))
                .thenThrow(new RuntimeException("Something unexpected happened!"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_LCVAPI_107.INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode_LCVAPI_107.INTERNAL_SERVER_ERROR.getDefaultMessage()))
                .andExpect(jsonPath("$.requestId").value(MOCKED_REQUEST_ID));
    }

}
src/test/java/com/example/auth/service/AuthenticationService_LCVAPI_118Test.java