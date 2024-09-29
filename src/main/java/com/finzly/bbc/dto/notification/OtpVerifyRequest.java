package com.finzly.bbc.dto.notification;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String userId;
    private String otpCode;
}
