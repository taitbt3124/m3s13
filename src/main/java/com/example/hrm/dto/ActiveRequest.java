package com.example.hrm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveRequest {
    @NotBlank(message = "Email không được trống")
    private String email;
    @NotBlank(message = "Mã OTP không được trống")
    private String otp;
}