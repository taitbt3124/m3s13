package com.example.hrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployeeUpdateDTO {

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 5, message = "Tên phải từ 5 ký tự trở lên")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email sai định dạng")
    private String email;

    private MultipartFile avatarFile;
}