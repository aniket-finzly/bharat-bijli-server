package com.finzly.bbc.controllers;

import com.finzly.bbc.dto.notification.OtpSendRequestUserId;
import com.finzly.bbc.dto.notification.OtpVerifyRequest;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.notification.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/otp")
@RequiredArgsConstructor
@Tag(name = "OTP API", description = "API for generating, resending, and verifying OTPs")
public class OtpController {

    private final OtpService otpService;

    /**
     * Generate a new OTP for a user (Customer or Employee)
     */
    @PostMapping("/generate-otp")
    @Operation(summary = "Generate OTP", description = "Generates a new OTP for the given user ID.")
    public ResponseEntity<ApiResponse<String>> generateOtp (@RequestBody OtpSendRequestUserId request) {
        otpService.generateOtp (request.getUserId ());
        return ResponseEntity.ok (ApiResponse.success ("OTP generated successfully.", null));
    }

    /**
     * Resend OTP to a user
     */
    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Resends an OTP if still valid or regenerates a new one.")
    public ResponseEntity<ApiResponse<String>> resendOtp (@RequestBody OtpSendRequestUserId request) {
        otpService.resendOtp (request.getUserId ());
        return ResponseEntity.ok (ApiResponse.success ("OTP resent successfully.", null));
    }

    /**
     * Verify OTP for a user
     */
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP for the given user ID and OTP code.")
    public ResponseEntity<ApiResponse<String>> verifyOtp (@RequestBody OtpVerifyRequest request) {
        try {
            String result = otpService.verifyOtp (request.getUserId (), request.getOtpCode ());
            return ResponseEntity.ok (ApiResponse.success ("OTP verified successfully.", result));
        } catch (Exception e) {
            log.error ("Error verifying OTP: {}", e.getMessage ());
            return ResponseEntity.badRequest ().body (ApiResponse.error ("OTP verification failed.", 400));
        }
    }
}
