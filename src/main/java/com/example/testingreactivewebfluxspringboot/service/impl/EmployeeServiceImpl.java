package com.example.testingreactivewebfluxspringboot.service.impl;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.entity.Employee;
import com.example.testingreactivewebfluxspringboot.mapper.EmployeeMapper;
import com.example.testingreactivewebfluxspringboot.repository.EmployeeRepository;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> getEmployeeById(String id){
        Mono<Employee> savedEmployee = employeeRepository.findById(id);
        return savedEmployee.map((employee) -> EmployeeMapper.mapToEmployeeDto(employee));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees(){
        Flux<Employee> employees = employeeRepository.findAll();
        return employees.map((employee) -> EmployeeMapper.mapToEmployeeDto(employee));
    }

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto){

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);

        return savedEmployee.map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }

}
