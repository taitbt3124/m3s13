package com.example.hrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username không được trống")
    private String username;

    @NotBlank(message = "Password không được trống")
    @Size(min = 6, message = "Password tối thiểu 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được trống")
    private String fullName;

    @NotBlank(message = "Email không được trống")
    private String email;

}