package com.finzly.bbc.controllers;

import com.finzly.bbc.dto.notification.OtpSendRequestUserId;
import com.finzly.bbc.dto.notification.OtpVerifyRequest;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.notification.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/generate-otp")
    @Operation(summary = "Generate OTP", description = "Generates a new OTP for the given user ID.")
    public ResponseEntity<CustomApiResponse<String>> generateOtp(@RequestBody OtpSendRequestUserId request) {
        otpService.generateOtp(request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomApiResponse.success("OTP generated successfully.", null, HttpStatus.CREATED.value()));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Resends an OTP if still valid or regenerates a new one.")
    public ResponseEntity<CustomApiResponse<String>> resendOtp(@RequestBody OtpSendRequestUserId request) {
        otpService.resendOtp(request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.success("OTP resent successfully.", null, HttpStatus.OK.value()));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP for the given user ID and OTP code.")
    public ResponseEntity<CustomApiResponse<String>> verifyOtp(@RequestBody OtpVerifyRequest request) {
        String result = otpService.verifyOtp(request.getUserId(), request.getOtpCode());
        return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.success("OTP verified successfully.", result, HttpStatus.OK.value()));
    }
}
