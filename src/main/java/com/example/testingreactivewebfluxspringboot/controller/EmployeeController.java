package com.example.testingreactivewebfluxspringboot.controller;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping
  public Flux<EmployeeDto> getAllEmployees() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("/{id}")
  public Mono<EmployeeDto> getEmployeeById(@PathVariable String id) {
    return employeeService.getEmployeeById(id);
  }

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
    return employeeService.saveEmployee(employeeDto);
  }

  @PutMapping("/{id}")
  public Mono<EmployeeDto> updateEmployee(@PathVariable String id,
      @RequestBody EmployeeDto employeeDto) {
    return employeeService.updateEmployee(id, employeeDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public Mono<Void> deleteEmployee(@PathVariable String id) {
    return employeeService.deleteEmployee(id);
  }
}
