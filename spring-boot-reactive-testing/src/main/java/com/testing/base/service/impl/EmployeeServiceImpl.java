package com.testing.base.service.impl;

import com.testing.base.dto.EmployeeDto;
import com.testing.base.entity.Employee;
import com.testing.base.mapper.EmployeeMapper;
import com.testing.base.repository.EmployeeRepository;
import com.testing.base.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(String employeeId) {
        Mono<Employee> employeeFoundInDatabase = employeeRepository.findById(employeeId);
        return employeeFoundInDatabase
                .map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = employeeRepository.findAll();
        return employeeFlux
                .map(EmployeeMapper::mapToEmployeeDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(String employeeId, EmployeeDto updatedEmployee) {
        Mono<Employee> employeeFoundInDatabase = employeeRepository.findById(employeeId)
                .switchIfEmpty(Mono.empty());
        return employeeFoundInDatabase.flatMap((existingEmployee) -> {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setEmail(updatedEmployee.getEmail());

            return employeeRepository.save(existingEmployee);
        }).map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<Void> deleteEmployeeById(String employeeId) {
        return employeeRepository.deleteById(employeeId)
                .switchIfEmpty(Mono.empty());
    }
}
