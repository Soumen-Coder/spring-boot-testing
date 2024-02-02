package com.testing.base.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.base.springboot.model.Employee;
import com.testing.base.springboot.service.EmployeeService;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService; //Since EmployeeController requires EmployeeService to be invoked as a constructor, hence we had to inject it as @WebMvcTest doesn't create load any other beans(service/repository) other than the controller in the controller layer.
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();
    }

    //Junit for Junit test for createEmployee operation of EmployeeController
    @DisplayName("Junit test for createEmployee operation of EmployeeController")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(print()) // to print the response of the RESTAPI
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    //Junit for getAllEmployees operation
    @DisplayName("Junit test for getAllEmployees operation")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = List.of(employee, Employee.builder().firstName("apache").lastName("maven").email("apache.maven@gmail.com").build());
        given(employeeService.getAllEmployees())
                .willReturn(employeeList);
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));
        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }
    /*
    Differences:
            Specificity:
            isOk() specifically asserts that the response status code is exactly 200 (OK).
            is2xxSuccessful() asserts that the response status code falls within the 2xx range (200-299), indicating a successful operation.
            When to use each:
            Use isOk() when you strictly expect a 200 status code.
            Use is2xxSuccessful() when any successful status code in the 2xx range is acceptable.
     */

    @DisplayName("Junit test for getEmployeeById operation")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        //given - precondition or setup
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", Collections.singletonMap("id", employee.getId())));
        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    @DisplayName("Junit test for getEmployeeById operation negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNothing() throws Exception {
        Long employeeId = UUID.randomUUID().getMostSignificantBits();
        //given - precondition or setup
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Junit test for updateEmployee operation")
    @Test
    public void givenEmployeeIdAndEmployeeObj_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        Employee savedEmployee = Employee.builder().id(1L).firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        Employee updatedEmployee = Employee.builder().id(1L).firstName("Apache").lastName("Maven").email("Apache.Maven@gmail.com").build();
        long employeeId = 1L;

        //given - precondition or setup
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Junit test for updateEmployee operation negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        Employee savedEmployee = Employee.builder().id(1L).firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        Employee updatedEmployee = Employee.builder().id(1L).firstName("Apache").lastName("Maven").email("Apache.Maven@gmail.com").build();
        long employeeId = UUID.randomUUID().getMostSignificantBits();

        //given - precondition or setup
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Junit test for deleteEmployeeById operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() throws Exception {
        Long employeeId = 1L;

        //given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployeeById(employeeId);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}


//In order to only unit test controller layer components, we're going to use @WebMvcTest annotation.
//Spring provides @WebMvcTest annotation to test spring MVC controllers.
//@WebMvcTest annotation doesn't load the other layer components like service layer components or repository layer components.
//It only loads the required controllers in controller layer.
//Hamcrest library is used to test the REST APIs. Hamcrest library basically provides, matchers which can be used in an assert statements to just match the actual or expected output.
//SYNTAX = assertThat(ACTUAL, is(EXPECTED));
//Hamcrest library are commonly used with JUnit and other testing framework for making assertions.
//Hamcrest Library's is() method is used to test the actual results with the expected result.
//JsonPath library is a Java DSL for reading JSON documents.
//Whenever we write the unit test to test the REST APIs, we have to test the response of REST APIs and the response basically contains the JSON and we want to validate the JSON values.
//So in order to retrieve the JSON values from the JSON object, we will use the JsonPath library.
//We can use a $ symbol to retrieve a whole JSON and a $ symbol is a root structure, regardless if it's an object or an array.
//And if you want to get the first name, you can use the syntax like $.first name="Apache".
//@WebMvcTest makes JUnit test cases much faster because it will load only specific controller and its dependencies, it doesn't load an entire application.
//Whenever we use @WebMvcTest annotation to test Spring MVC controller, then spring boot instantiate only the web layer rather than the entire application context.
//Typically, we will have a lot of Spring MVC controllers in a spring boot applications. Let's say we have a home controller, employee controller and user controller.
//And if we want to unit test only one controller, let's say home controller, then @WebMvcTest annotation provides a flexibility to load only the home controller. It doesn't load the employee controller and user controller by using @WebMvcTest(HomeController.class)
//Spring boot provides @SpringBootTest annotation and this annotation is really useful for integration testing.
//In case of integration test we basically test different layers. For example controller layer, service layer, repository layer and then database.
//In case of integration testing, we will test the complete flow from controller layer to database.
//@SpringBootTest annotation doesn't load specific components or beans like in case of @WebMvcTest, but it will load the entire application context.



//Usage of Testcontainers and solutions to problems associated with Integration tests is mentioned below.
//Within the Integration tests, we have used a locally installed MySQL database for integration testing.
//Well, this integration testing has some drawbacks.
//In order to run integration test cases first, we need to install MySQL database. It means that our integration tests depends on the external services.
//Consider, we are developing a large enterprise application, then our enterprise application may depend on various external services, for instance, databases, message queues and a lot of other external services.
//If we develop the integration test cases, then we need to install all these external services locally or any region that we are interested in testing, and then only we can enable our integration test cases.
//If we want to run our integration test cases on a different machines, then also we need to install all the external services, then only again able to run our integration tests.
//The setup-time, maintenance and same version to be installed in all places is a headache.
//Solution is Testcontainers.
//Test Containers is a Java library that supports JUnit tests, providing lightweight throwaways, instances of common databases, Selenium web browser or anything else that can run in a Docker container.
//Test containers allow us to use Docker containers in JUnit test cases itself.
//If we use the test containers then test containers will basically download the Docker image that is MySQL Docker image, as in our case, from the Docker Hub, and then it will deploy it in a Docker container that can be used to run our integration test cases with respect to MySQL database that is installed in a Docker container. It will start the Docker container for us.
//And once all the integration test cases has completed execution, the test container, will stop the Docker containers.
//It provides us a way to be able to run integration test without the need of pre-installed(external dependent) component.
//Using test containers, we will always start with a clean database and our integration test could run on any machine.
//If we want to run our integration test cases in a production using Jenkins, via continuous integration pipeline then, this test containers will help a lot if we have a Docker integrated with Jenkins CI
//Containers will simply pull the Docker images from the Docker hub and will create the Docker container instances and, will deploy the required Docker images to run the integration test cases.
//Testcontainer allows us to use our Docker containers within our tests.As a result, we can write a self-contained integration test that depends on the external services.
//Testcontainers can be integrated with JUnit 4 or JUnit 5 or spock.