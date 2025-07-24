src/main/java/com/example/service/interfaces/IOtpService_J1K2L.java

<ctrl62>
<ctrl60>
package com.example.service.interfaces;

import com.example.model.User_M1N2O;

public interface IOtpService_J1K2L {
    void generateAndSendOtp(User_M1N2O user);
    boolean verifyOtp(User_M1N2O user, String otp);
}