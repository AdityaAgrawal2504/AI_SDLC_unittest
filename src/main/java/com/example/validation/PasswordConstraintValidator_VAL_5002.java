package com.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordConstraintValidator_VAL_5002 implements ConstraintValidator<ValidPassword_VAL_5001, String> {

    // Regex: At least 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
```
```java
//
// Filename: src/test/java/com/example/controller/UserAuthController_UATH_1001Test.java
//