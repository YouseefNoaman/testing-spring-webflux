package com.example.testingreactivewebfluxspringboot.controller;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
  public Mono<ResponseEntity<EmployeeDto>> getEmployeeById(@PathVariable String id) {

    return employeeService.getEmployeeById(id).flatMap(employeeDto -> {
      // Handle the case where the employee is present
      // Perform your logic here
      return Mono.just(ResponseEntity.ok(employeeDto));
    }).switchIfEmpty(Mono.defer(() -> {
      // Handle the case where the employee is not found
      // Perform alternative logic here
      return Mono.just(ResponseEntity.notFound().build());
    }));
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
