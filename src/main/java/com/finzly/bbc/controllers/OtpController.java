package com.finzly.bbc.controllers;

import aj.org.objectweb.asm.commons.TryCatchBlockSorter;
import com.finzly.bbc.dto.notification.OtpSendRequestEmail;
import com.finzly.bbc.dto.notification.OtpSendRequestUserId;
import com.finzly.bbc.dto.notification.OtpVerifyRequest;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.notification.EmailService;
import com.finzly.bbc.services.notification.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OtpController {

    private final EmailService emailService;
    private final OtpService otpService;

    @PostMapping("/send-otp-user")
    public ResponseEntity<ApiResponse<String>> sendOtp (@RequestBody OtpSendRequestUserId request) {
        otpService.sendOtpToUser (request.getUserId ());
        return ResponseEntity.ok (ApiResponse.success ("OTP sent successfully.", null));
    }

    @PostMapping("/verify-otp-user")
    public ResponseEntity<ApiResponse<String>> verifyOtp (@RequestBody OtpVerifyRequest request) {
        try {
        String result =   otpService.verifyOtpForUser (request.getUserId (), request.getOtpCode ());
            return ResponseEntity.ok (ApiResponse.success ("OTP verified successfully.", result));
        } catch (Exception e) {
            return ResponseEntity.ok (ApiResponse.error ("OTP verification failed.", 400));
        }
    }

    @PostMapping("/test-email")
    public ResponseEntity<ApiResponse<String>> testEmail (@RequestBody OtpSendRequestEmail request) {
        emailService.sendOtpEmail (request.getEmail (), "123456");
        return ResponseEntity.ok (ApiResponse.success ("Email sent successfully.", null));
    }
}
