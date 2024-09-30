package com.finzly.bbc.exceptions.custom.otp;

public class OtpExpiredException extends RuntimeException {
    public OtpExpiredException (String message) {
        super (message);
    }
}
