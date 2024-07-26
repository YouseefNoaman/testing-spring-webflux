package com.example.testingreactivewebfluxspringboot;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.testingreactivewebfluxspringboot.controller.EmployeeController;
import com.example.testingreactivewebfluxspringboot.dto.EmployeeDto;
import com.example.testingreactivewebfluxspringboot.service.EmployeeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  // this is used instead of @Mock because of the need to inject the service into the context
  private EmployeeService employeeService;

  private static final String URL = "/api/v1/employees";

  private static EmployeeDto employeeDto;

  @BeforeEach
  public void setup() {
    employeeDto = new EmployeeDto();
    employeeDto.setFirstName("John");
    employeeDto.setLastName("Doe");
    employeeDto.setEmail("sfjh@gmail.com");

  }

  @Test
  public void givenEmployeeObj_whenSaveEmployee_thenReturnEmployeeObj() {

    BDDMockito.given(employeeService.saveEmployee(any(EmployeeDto.class)))
        .willReturn(Mono.just(employeeDto));

    // when - action or behavior

    WebTestClient.ResponseSpec response = webTestClient.post().uri(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(employeeDto), EmployeeDto.class)
        .exchange();

    // then - verify the result

    response.expectStatus().isCreated().expectBody()
        .consumeWith(System.out::println)
        .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
        .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
        .jsonPath("$.email").isEqualTo(employeeDto.getEmail());

  }

  @Test
  public void givenEmployeeObj_whenSaveEmployee_thenReturnEmployeeObj_AnotherApproach() {

    // Mock the service call
    when(employeeService.saveEmployee(any(EmployeeDto.class))).thenReturn(Mono.just(employeeDto));

    // Call the controller method
    webTestClient.post()
        .uri(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(employeeDto), EmployeeDto.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(EmployeeDto.class)
        .isEqualTo(employeeDto);
  }

  @DisplayName("JUnit test for get all employees")
  @Test
  public void givenEmployeeObjs_whenGettingEmployeeList_thenReturnFluxOfEmployees() {
    // given - precondition or setup

    EmployeeDto employeeDto2 = new EmployeeDto();
    employeeDto2.setFirstName("fred");
    employeeDto2.setLastName("hans");
    employeeDto2.setEmail("fredH@gmail.com");

    Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(List.of(employeeDto, employeeDto2));

    when(employeeService.getAllEmployees()).thenReturn(employeeDtoFlux);

    // when - action that will be tested

    webTestClient.get()
        .uri(URL)
        .exchange()
        // then - the expected output
        .expectStatus().isOk()
        .expectBodyList(EmployeeDto.class)
        .hasSize(2)
        .contains(employeeDto, employeeDto2);
  }

  @Test
  public void getAllEmployees_Success() {
    // given - precondition or setup

    EmployeeDto employeeDto2 = new EmployeeDto();
    employeeDto2.setFirstName("fred");
    employeeDto2.setLastName("hans");
    employeeDto2.setEmail("fredH@gmail.com");

    // Mock the service call
    when(employeeService.getAllEmployees()).thenReturn(Flux.just(employeeDto, employeeDto2));

    // Call the controller method
    webTestClient.get()
        .uri(URL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(EmployeeDto.class)
        .consumeWith(System.out::println)
        .hasSize(2)
        .contains(employeeDto, employeeDto2);
  }

/*
  @Test
  public void getEmployeeById_SuccessByMe() {


    // Mock the service call
    when(employeeService.getEmployeeById("1")).thenReturn(Mono.just(employeeDto));

    // Call the controller method
    webTestClient.get()
        .uri(URL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody().consumeWith(System.out::println)
        .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
        .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
        .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
  }
*/

  @Test
  public void getEmployeeById_Success() {
    // Mock the service call
    when(employeeService.getEmployeeById(anyString())).thenReturn(Mono.just(employeeDto));

    // Call the controller method
    webTestClient.get()
        .uri(URL + "/{id}", "1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(EmployeeDto.class)
        .isEqualTo(employeeDto);
  }

  @Test
  public void getEmployeeById_NotFound() {
    // Mock the service call
    when(employeeService.getEmployeeById(anyString())).thenReturn(Mono.empty());

    // Call the controller method
    webTestClient.get()
        .uri(URL + "/{id}", "1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void givenEmployeeObjAndId_whenUpdateEmployeeBy_ThenReturnEmployeeObj() {
    EmployeeDto employeeDto2 = new EmployeeDto();
    employeeDto2.setFirstName("fred");
    employeeDto2.setLastName("hans");
    employeeDto2.setEmail("fredH@gmail.com");

    // Mock the service call
    when(employeeService.updateEmployee(anyString(), ArgumentMatchers.any(EmployeeDto.class)))
        .thenReturn(Mono.just(employeeDto2));

    // Call the controller method
    webTestClient.put()
        .uri(URL + "/{id}", "1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(employeeDto), EmployeeDto.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(EmployeeDto.class)
        .isEqualTo(employeeDto2);
  }

  @Test
  public void deleteEmployee_Success() {
    // Mock the service call
    when(employeeService.deleteEmployee(anyString())).thenReturn(Mono.empty());

    // Call the controller method
    webTestClient.delete()
        .uri(URL + "/{id}", "1")
        .exchange()
        .expectStatus().isNoContent();
  }

}
