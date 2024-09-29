package com.finzly.bbc.exceptions.custom.otp;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
