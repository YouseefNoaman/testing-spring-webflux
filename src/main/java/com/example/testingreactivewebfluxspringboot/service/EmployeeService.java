package com.example.testingreactivewebfluxspringboot.service;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
}
