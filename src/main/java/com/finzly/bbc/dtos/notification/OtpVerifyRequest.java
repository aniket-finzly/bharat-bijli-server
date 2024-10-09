package com.finzly.bbc.dtos.notification;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String userId;
    private String otpCode;
}
