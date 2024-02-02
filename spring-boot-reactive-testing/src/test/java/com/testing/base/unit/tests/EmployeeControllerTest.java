package com.testing.base.unit.tests;

import com.testing.base.controller.EmployeeController;
import com.testing.base.dto.EmployeeDto;
import com.testing.base.service.EmployeeService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class) //SpringExtension integrates the Spring TestContext Framework into JUnit 5's Jupiter programming model.
@WebFluxTest(controllers = EmployeeController.class) //In order to test spring webflux controllers we have @WebFluxTest annotation and it loads only the necessary beans that is passed to the controllers variable.
//It doesn't load any other beans from any other layers like service layer or repository layer, which allows Junit to run faster
//It autoconfigures the WebTestClient class to call the reactive rest apis
public class EmployeeControllerTest {
    @Autowired
    private WebTestClient webTestClient; //This will be autoconfigured by @WebFluxTest and it helps in creating the http requests and make reactive api calls.

    @MockBean
    private EmployeeService employeeService; // We could have used the @Mock annotation here, but we want to register this bean to the application context and hence @MockBean helps with that

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        //given - preconditions or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        //when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        //then - verify the result or output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnSavedEmployee() {
        //given - preconditions or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("123");
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        BDDMockito.given(employeeService.getEmployeeById(ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        //when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", employeeDto.getId())) // to pass the id dynamically Collections utility is being used
                .exchange();

        //then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnListOfEmployees() {
        //given - preconditions or setup
        List<EmployeeDto> employeeList = getEmployeeDTOs();

        Flux<EmployeeDto> employeeFlux = Flux.fromIterable(employeeList);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeFlux);

        //when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then - verify the result or output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void givenEmployeeIdAndUpdatedEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - preconditions or setup
        EmployeeDto updatedEmployeeDto = new EmployeeDto();
        updatedEmployeeDto.setId("123");
        updatedEmployeeDto.setFirstName("Apache");
        updatedEmployeeDto.setLastName("Maven");
        updatedEmployeeDto.setEmail("Apache.Maven@gmail.com");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(String.class), ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(updatedEmployeeDto));

        //when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", updatedEmployeeDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployeeDto), EmployeeDto.class)
                .exchange();

        //then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given - preconditions or setup
        Mono<Void> voidMono = Mono.empty();
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("123");
        employeeDto.setFirstName("Spring");
        employeeDto.setLastName("Boot");
        employeeDto.setEmail("Spring.Boot@gmail.com");

        BDDMockito.given(employeeService.deleteEmployeeById(ArgumentMatchers.any(String.class)))
                .willReturn(voidMono);

        //when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/api/employees/{id}", Collections.singletonMap("id", employeeDto.getId()))
                .exchange();

        //then - verify the result or output
        response.expectStatus().isNoContent()
                .expectBody(Void.class)
                .consumeWith(System.out::println);
    }

    private static List<EmployeeDto> getEmployeeDTOs() {
        List<EmployeeDto> employeeList = new ArrayList<>();
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Spring");
        employeeDto1.setLastName("Boot");
        employeeDto1.setEmail("Spring.Boot@gmail.com");
        employeeList.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Apache");
        employeeDto2.setLastName("Maven");
        employeeDto2.setEmail("Apache.Maven@gmail.com");
        employeeList.add(employeeDto2);
        return employeeList;
    }
}
