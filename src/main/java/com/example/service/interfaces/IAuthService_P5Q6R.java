src/main/java/com/example/service/interfaces/IAuthService_P5Q6R.java

<ctrl60>
<ctrl62>
<ctrl61>
package com.example.service.interfaces;

import com.example.dto.LoginInitiateDto_G5H6I;
import com.example.dto.SessionDto_M9N0O;
import com.example.dto.SessionVerifyDto_J7K8L;
import com.example.model.User_M1N2O;

public interface IAuthService_P5Q6R {
    void initiateLogin(LoginInitiateDto_G5H6I loginInitiateDto);
    SessionDto_M9N0O verifyOtpAndCreateSession(SessionVerifyDto_J7K8L sessionVerifyDto);
    void logout(User_M1N2O currentUser);
}