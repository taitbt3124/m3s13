package com.example.hrm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username không được trống")
    private String username;

    @NotBlank(message = "Password không được trống")
    private String password;
}