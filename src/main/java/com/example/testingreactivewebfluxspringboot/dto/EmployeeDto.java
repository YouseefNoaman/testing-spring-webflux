package com.example.testingreactivewebfluxspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

  private String id;
  private String firstname;
  private String lastname;
  private String email;
}
