package com.finzly.bbc.exceptions.custom.otp;

public class OtpGenerationException extends RuntimeException {
    public OtpGenerationException(String message) {
        super(message);
    }
}
