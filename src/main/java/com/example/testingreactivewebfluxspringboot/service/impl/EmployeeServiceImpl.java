package com.example.testingreactivewebfluxspringboot.service.impl;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.entity.Employee;
import com.example.testingreactivewebfluxspringboot.mapper.EmployeeMapper;
import com.example.testingreactivewebfluxspringboot.repository.EmployeeRepository;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto){

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);

        return savedEmployee.map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }

}
