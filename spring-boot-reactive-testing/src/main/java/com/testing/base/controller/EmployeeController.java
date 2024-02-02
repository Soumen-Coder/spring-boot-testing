package com.testing.base.controller;

import com.testing.base.dto.EmployeeDto;
import com.testing.base.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.saveEmployee(employeeDto);
    }

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<EmployeeDto> getEmployeeById(@PathVariable("id") String employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("{id}")
    public Mono<EmployeeDto> updateEmployee(@PathVariable("id") String employeeId, @RequestBody EmployeeDto updatEmployeeDto) {
        return employeeService.updateEmployee(employeeId, updatEmployeeDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployeeById(@PathVariable("id") String employeeId) {
        return employeeService.deleteEmployeeById(employeeId);
    }
}
