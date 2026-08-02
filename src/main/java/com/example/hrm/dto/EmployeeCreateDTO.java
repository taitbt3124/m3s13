package com.example.hrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployeeCreateDTO {

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email sai định dạng")
    private String email;

    @NotBlank(message = "Phòng ban không được để trống")
    private String department;

    private MultipartFile avatarFile;
}