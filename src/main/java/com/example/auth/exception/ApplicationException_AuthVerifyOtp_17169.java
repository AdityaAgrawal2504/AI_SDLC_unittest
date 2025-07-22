package com.example.auth.exception;

import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import lombok.Getter;

/**
 * Custom runtime exception used for handling application-specific errors.
 */
@Getter
public class ApplicationException_AuthVerifyOtp_17169 extends RuntimeException {

    private final ErrorCode_AuthVerifyOtp_17169 errorCode;

    public ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169 errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
src/main/java/com/example/auth/entity/OtpRecord_17169.java