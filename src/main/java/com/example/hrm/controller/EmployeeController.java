package com.example.hrm.controller;

import com.example.hrm.dto.EmployeeCreateDTO;
import com.example.hrm.dto.EmployeeUpdateDTO;
import com.example.hrm.entity.Employee;
import com.example.hrm.repository.EmployeeRepository;
import com.example.hrm.service.CloudinaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEmployee(@Valid @ModelAttribute EmployeeCreateDTO dto) throws IOException {
        String avatarUrl = cloudinaryService.uploadAvatar(dto.getAvatarFile());

        Employee employee = Employee.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .avatarUrl(avatarUrl)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRepository.save(employee));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @ModelAttribute EmployeeUpdateDTO dto) throws IOException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        if (dto.getAvatarFile() != null && !dto.getAvatarFile().isEmpty()) {
            String newAvatarUrl = cloudinaryService.uploadAvatar(dto.getAvatarFile());
            employee.setAvatarUrl(newAvatarUrl);
        }

        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());

        return ResponseEntity.ok(employeeRepository.save(employee));
    }
}