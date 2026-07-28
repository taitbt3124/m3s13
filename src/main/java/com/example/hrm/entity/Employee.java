package com.example.hrm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @Positive(message = "Lương phải lớn hơn 0")
    private Double salary;
}