package com.finzly.bbc.exceptions.custom.otp;

public class OtpNotFoundException extends RuntimeException {
    public OtpNotFoundException (String message) {
        super (message);
    }
}


