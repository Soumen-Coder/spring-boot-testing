package com.testing.base.integration.tests;

import com.testing.base.dto.EmployeeDto;
import com.testing.base.repository.EmployeeRepository;
import com.testing.base.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //It will load the full application context, in order to test the complete flow from controller to repository
//It won't start the server, we have to configure a web environment attribute
public class EmployeeControllerIntegrationTests {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient; //WebFlux module provides WebTestClient class to test reactive APIs that are developed using WebFlux Modules

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void before() {
        System.out.println("Before each tests, deleting records");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    public void testSaveEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        webTestClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetEmployeeById() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("123");
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(savedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

    @Test
    public void testGetAllEmployees() {
        List<EmployeeDto> employeeList = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setId("123");
        employeeDto1.setFirstName("Spring");
        employeeDto1.setLastName("Boot");
        employeeDto1.setEmail("Spring.Boot@gmail.com");
        employeeList.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setId("123");
        employeeDto2.setFirstName("Spring");
        employeeDto2.setLastName("Boot");
        employeeDto2.setEmail("Spring.Boot@gmail.com");
        employeeList.add(employeeDto2);

        employeeService.saveEmployee(employeeDto1).block();
        employeeService.saveEmployee(employeeDto2).block();

        webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateEmployees() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("123");
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Apache");
        updatedEmployee.setLastName("Maven");
        updatedEmployee.setEmail("Apache.Maven@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.put()
                .uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());

    }

    @Test
    public void testDeleteEmployees() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class)
                .consumeWith(System.out::println);
    }
}
