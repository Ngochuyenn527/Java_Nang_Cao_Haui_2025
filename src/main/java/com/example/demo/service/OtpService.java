package com.example.demo.service;

public interface OtpService {
    String generateOtp(String email);
    boolean validateOtp(String email, String otp);
}
