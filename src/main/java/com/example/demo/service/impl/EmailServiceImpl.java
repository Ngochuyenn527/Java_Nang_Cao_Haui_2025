package com.example.demo.service.impl;

import com.example.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Mã xác thực OTP");
        msg.setText("Mã OTP của bạn là: " + otp);
        mailSender.send(msg);
    }
}