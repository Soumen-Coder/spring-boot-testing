package com.testing.base.springboot.service;

import com.testing.base.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployeeById(Long id);
}
