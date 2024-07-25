package com.example.testingreactivewebfluxspringboot.repository;

import com.example.testingreactivewebfluxspringboot.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {
}
