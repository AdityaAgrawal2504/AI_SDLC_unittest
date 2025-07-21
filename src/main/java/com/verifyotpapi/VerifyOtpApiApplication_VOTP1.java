package com.verifyotpapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.verifyotpapi.config")
public class VerifyOtpApiApplication_VOTP1 {

	public static void main(String[] args) {
		SpringApplication.run(VerifyOtpApiApplication_VOTP1.class, args);
	}

}
```
```java
// src/main/java/com/verifyotpapi/config/AppProperties_VOTP1.java