package com.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    private static final Duration OTP_EXPIRY =
            Duration.ofMinutes(5);

    public void sendOtp(String email) {

        String otp = generateOtp();

        redisTemplate.opsForValue().set(
                "otp:" + email,
                otp,
                OTP_EXPIRY
        );

        emailService.sendEmail(
                email,
                "BookMyShow OTP",
                """
                Your BookMyShow verification OTP is %s.

                This OTP is valid for 5 minutes.

                If you did not request this OTP, please ignore this email.
                """.formatted(otp)
        );
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        String key = "otp:" + email;

        String storedOtp =
                redisTemplate.opsForValue().get(key);

        if (storedOtp == null) {
            return false;
        }

        if (!storedOtp.equals(otp)) {
            return false;
        }

        redisTemplate.delete(key);

        return true;
    }

    private String generateOtp() {

        int otp = ThreadLocalRandom.current()
                .nextInt(100000, 1000000);

        return String.valueOf(otp);
    }
}