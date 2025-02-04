package com.example.testingreactivewebfluxspringboot.mapper;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.entity.Employee;

public class EmployeeMapper {
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                employee.getFirstName(), employee.getLastName(),
                employee.getEmail());
    }
    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return new Employee(employeeDto.getId(),
                employeeDto.getFirstname(), employeeDto.getLastname(),
                employeeDto.getEmail());
    }
}
