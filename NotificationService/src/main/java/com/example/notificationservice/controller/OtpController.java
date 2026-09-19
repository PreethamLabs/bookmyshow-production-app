package com.example.notificationservice.controller;

import com.example.notificationservice.dto.OtpResponse;
import com.example.notificationservice.dto.OtpSendRequest;
import com.example.notificationservice.dto.OtpVerifyRequest;
import com.example.notificationservice.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public OtpResponse sendOtp(
            @Valid @RequestBody OtpSendRequest request) {

        otpService.sendOtp(request.getEmail());

        return OtpResponse.builder()
                .verified(false)
                .message("OTP sent successfully")
                .build();
    }

    @PostMapping("/verify")
    public OtpResponse verifyOtp(
            @Valid @RequestBody OtpVerifyRequest request) {

        boolean verified =
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );

        return OtpResponse.builder()
                .verified(verified)
                .message(
                        verified
                                ? "OTP verified successfully"
                                : "Invalid or expired OTP"
                )
                .build();
    }
}