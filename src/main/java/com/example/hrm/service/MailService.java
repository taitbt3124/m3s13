package com.example.hrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã OTP kích hoạt tài khoản");
        message.setText("Mã OTP của bạn là: " + otp + ". Hiệu lực trong 5 phút.");
        mailSender.send(message);
    }
}