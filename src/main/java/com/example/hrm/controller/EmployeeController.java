package com.example.hrm.controller;

import com.example.hrm.entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = List.of(
                new Employee(1L, "Nguyen Van A", 15000000.0),
                new Employee(2L, "Tran Thi B", 18000000.0),
                new Employee(3L, "Le Van C", 12000000.0)
        );
        return ResponseEntity.ok(employees);
    }
}