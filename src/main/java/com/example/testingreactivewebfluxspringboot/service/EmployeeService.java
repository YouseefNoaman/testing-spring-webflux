package com.example.testingreactivewebfluxspringboot.service;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> getEmployeeById(String id);
    Flux<EmployeeDto> getAllEmployees();
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto);
}
