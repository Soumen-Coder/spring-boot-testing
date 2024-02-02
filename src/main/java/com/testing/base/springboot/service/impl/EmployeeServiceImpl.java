package com.testing.base.springboot.service.impl;

import com.testing.base.springboot.exception.EmployeeServiceException;
import com.testing.base.springboot.model.Employee;
import com.testing.base.springboot.repository.EmployeeRepository;
import com.testing.base.springboot.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(optionalEmployee.isPresent()){
            throw new EmployeeServiceException("Employee already exists with email :- "+employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}