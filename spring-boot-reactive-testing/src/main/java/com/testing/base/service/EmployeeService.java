package com.testing.base.service;

import com.testing.base.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployeeById(String employeeId);
    Flux<EmployeeDto> getAllEmployees();
    Mono<EmployeeDto> updateEmployee(String employeeId, EmployeeDto updatedEmployee);
    Mono<Void> deleteEmployeeById(String employeeId);
}
