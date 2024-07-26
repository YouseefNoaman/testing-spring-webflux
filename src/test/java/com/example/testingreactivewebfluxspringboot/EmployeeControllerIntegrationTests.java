package com.example.testingreactivewebfluxspringboot;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// for the  integration tests to load context, and to boot the project as a web app
public class EmployeeControllerIntegrationTests {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private WebTestClient webTestClient;

  private EmployeeDto employeeDto;

  private static final String URL = "/api/v1/employees";

  @BeforeEach
  public void setUp() {
    // use block() instead of subscribe() because this needs to be done before the tests running
    employeeService.deleteAll().block();

    employeeDto = new EmployeeDto();
    employeeDto.setId("1");
    employeeDto.setFirstname("John");
    employeeDto.setLastname("Doe");
    employeeDto.setEmail("john.doe@example.com");

    employeeService.saveEmployee(employeeDto).block();
  }

  @Test
  public void saveEmployeeIT() {

    // given

    EmployeeDto employeeDto = new EmployeeDto(null, "Jane", "Doe", "jane.doe@example.com");


    webTestClient.post().uri(URL).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(employeeDto), EmployeeDto.class)
        .exchange().expectStatus().isCreated()
        .expectBody().consumeWith(System.out::println)
        .jsonPath("$").isNotEmpty()
        .jsonPath("$.firstname").isEqualTo(employeeDto.getFirstname())
        .jsonPath("$.lastname").isEqualTo(employeeDto.getLastname())
        .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
  }

  @Test
  public void getEmployeeByIdIT() {

    webTestClient.get().uri(URL + "/{id}", employeeDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isOk()
        .expectBody().consumeWith(System.out::println)
        .jsonPath("$.firstname").isEqualTo(employeeDto.getFirstname())
        .jsonPath("$.lastname").isEqualTo(employeeDto.getLastname())
        .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
  }

  @Test
  public void getEmployeeById() {
    webTestClient.get()
        .uri(URL + "/{id}", employeeDto.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(EmployeeDto.class)
        .value(foundEmployee -> {
          assertThat(foundEmployee.getId()).isEqualTo(employeeDto.getId());
          assertThat(foundEmployee.getFirstname()).isEqualTo(employeeDto.getFirstname());
          assertThat(foundEmployee.getLastname()).isEqualTo(employeeDto.getLastname());
          assertThat(foundEmployee.getEmail()).isEqualTo(employeeDto.getEmail());
        });
  }

  @Test
  public void getAllEmployeesIT() {
    webTestClient.get()
        .uri(URL)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(EmployeeDto.class)
        .value(employees -> assertThat(employees).hasSize(1));
  }

  @Test
  public void updateEmployeeIT() {
    EmployeeDto employeeDtoToUse = new EmployeeDto(employeeDto.getId(), "John", "Smith", "john.smith@example.com");

    webTestClient.put()
        .uri(URL + "/{id}", employeeDtoToUse.getId())
        .body(Mono.just(employeeDtoToUse), EmployeeDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(EmployeeDto.class)
        .value(updatedEmployee -> {
          assertThat(updatedEmployee.getId()).isEqualTo(employeeDtoToUse.getId());
          assertThat(updatedEmployee.getFirstname()).isEqualTo("John");
          assertThat(updatedEmployee.getLastname()).isEqualTo("Smith");
          assertThat(updatedEmployee.getEmail()).isEqualTo("john.smith@example.com");
        });
  }

  @Test
  public void deleteEmployeeIT() {
    webTestClient.delete()
        .uri(URL + "/{id}", employeeDto.getId())
        .exchange()
        .expectStatus().isNoContent();

    webTestClient.get()
        .uri("/employees/{id}", employeeDto.getId())
        .exchange()
        .expectStatus().isNotFound();
  }

}
