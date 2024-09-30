package com.finzly.bbc.exceptions.custom.otp;

public class OtpAttemptLimitExceededException extends RuntimeException {
    public OtpAttemptLimitExceededException (String message) {
        super (message);
    }
}

