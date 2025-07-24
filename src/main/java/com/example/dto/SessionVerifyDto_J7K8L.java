src/main/java/com/example/dto/SessionVerifyDto_J7K8L.java
package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionVerifyDto_J7K8L {

    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @NotBlank(message = "OTP cannot be blank.")
    @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit string.")
    private String otp;
}