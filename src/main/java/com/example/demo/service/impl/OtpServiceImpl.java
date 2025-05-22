package com.example.demo.service.impl;

import com.example.demo.service.OtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpData> otpMap = new HashMap<>();

    @Override
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpMap.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        OtpData data = otpMap.get(email);
        if (data == null || data.expired.isBefore(LocalDateTime.now())) return false;
        return data.otp.equals(otp);
    }

    private static class OtpData {
        String otp;
        LocalDateTime expired;

        OtpData(String otp, LocalDateTime expired) {
            this.otp = otp;
            this.expired = expired;
        }
    }
}
