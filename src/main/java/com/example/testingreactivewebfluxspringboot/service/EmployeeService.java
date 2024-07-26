package com.example.testingreactivewebfluxspringboot.service;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

  Mono<EmployeeDto> getEmployeeById(String id);

  Flux<EmployeeDto> getAllEmployees();

  Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);

  Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto);

  Mono<Void> deleteEmployee(String id);

  Mono<Void> deleteAll();
}
