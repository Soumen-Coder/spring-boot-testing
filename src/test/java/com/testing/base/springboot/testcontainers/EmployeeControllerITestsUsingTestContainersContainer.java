package com.testing.base.springboot.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.base.springboot.model.Employee;
import com.testing.base.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers // used to integrate junit5 with testcontainers
public class EmployeeControllerITestsUsingTestContainersContainer extends AbstractionContainerBaseTest {
    //Pulls mysql docker image from docker-hub and creates a docker container and deploys mysql image in it to use it to run integration tests.
    //The field needs to be static, to be shared with all the test methods, otherwise if not static, the docker container will stop after each run of a test method and will start before each run of a test method
    //The @Testcontainers will manage all the lifecycle of the @Container instances
    /*@Container
    private static final MySQLContainer mySQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withDatabaseName("use_your_database_name_don't_hardcode_use_property_file")
                .withUsername("an_employee_don't_hardcode_use_property_file")
                .withPassword("needs_to_be_encrypted_please__don't_hardcode_use_property_file");*/

    //@DynamicPropertySource, dynamically fetches values from @Container, and loads them to the Spring Application Context
    //Suppose, there are multiple files that needs the mySQL_CONTAINER, then these values will be required and made available to the application context for other integration classes which is fulfilled by @DynamicPropertySource
   /* @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", mySQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", mySQL_CONTAINER::getPassword);
    }*/

    //Here, @Container or @TestContainer is not used because we have manually started the container in AbstractionBaseTest and don't want @Testcontainer to manage the lifecycle of the container.
    //This class has been extended by the parent AbstractionBaseTest which ahs the container creation and starting of it and sharing of the container using static field and following the singeton pattern for reuse.

    @Autowired
    private MockMvc mockMvc; //to make HTTP request using perform() method
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        System.out.println("Database Name : "+MY_SQL_CONTAINER.getDatabaseName());
        System.out.println("Database Username : "+MY_SQL_CONTAINER.getUsername());
        System.out.println("Database Password : "+MY_SQL_CONTAINER.getPassword());
        employeeRepository.deleteAll();
    }

    //Integration test for createEmployee operation of EmployeeController
    @DisplayName("Integration test for createEmployee operation of EmployeeController")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();

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

    //Integration test for getAllEmployees operation
    @DisplayName("Integration test for getAllEmployees operation")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList =
                List.of(Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build(),
                        Employee.builder().firstName("Apache").lastName("Maven").email("Apache.Maven@gmail.com").build(),
                        Employee.builder().firstName("Tony").lastName("Stark").email("Tomy.Stark@gmail.com").build());
        employeeRepository.saveAll(employeeList);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    //Integration test for getEmployeeById operation
    @DisplayName("Integration test for getEmployeeById operation")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

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

    //Integration test for getEmployeeById operation negative scenario
    @DisplayName("Integration test for getEmployeeById operation negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNothing() throws Exception {
        Long employeeId = UUID.randomUUID().getMostSignificantBits();
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Integration test for updateEmployee operation
    @DisplayName("Integration test for updateEmployee operation")
    @Test
    public void givenEmployeeIdAndEmployeeObj_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        Employee updatedEmployee = Employee.builder().firstName("Apache").lastName("Maven").email("Apache.Maven@gmail.com").build();
        employeeRepository.save(savedEmployee);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    //Integration test for updateEmployee operation negative scenario
    @DisplayName("Integration test for updateEmployee operation negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        Employee updatedEmployee = Employee.builder().firstName("Apache").lastName("Maven").email("Apache.Maven@gmail.com").build();
        employeeRepository.save(savedEmployee);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", UUID.randomUUID().getMostSignificantBits())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Integration test for deleteEmployeeById operation
    @DisplayName("Integration test for deleteEmployeeById operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder().firstName("Spring").lastName("Boot").email("Spring.Boot@gmail.com").build();
        employeeRepository.save(savedEmployee);

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}

//Integration tests focus on integrating different layers of the application that also means no mocking is involved.
//We write the integration tests to test different layers of the application.
//In case of unit testing we test the individual components, and one component can depend on another component, hence we have mocked the dependent dependencies.
//But, in case of integration testing, we don't have the mock the dependency. We're going to test the entire flow. We are going to basically write the JUnit test case to test the complete flow from controller layer to database.
//And basically, we write the integration test for testing a "feature" which may involve interaction with multiple components.
//@SpringBootTest - Spring boot provides excellent support for integration testing because spring boot provides @SpringBootTest annotation for integration testing, and this annotation creates application context and loads the full application context.
//@SpringBootTest annotation will load all the beans from all the layers into the application context.
//@SpringBootTest annotation does behind the scene is that it starts the embedded server, and it will load all the spring beans in application context by creating a creates a web environment and then enables @Test methods to do Integration testing.
//And by default @SpringBootTest annotation does not start the embedded server, we have to configure the web environment attribute to define how will the tests be run.
//A couple of values is provided for configuration of web environment attribute -> MOCK, RANDOM_PORT, DEFINED_PORT and NONE.
//We have to configure one of these as the web environment attribute value. Most of the time we are going to use RANDOM_PORT for web environment attribute to do the integration testing.
//If you don't use the web environment attribute in @SpringBootTest annotation then the MOCK value will be by default assigned to the web environment attribute.
//MOCK value -> Loads a web application context and provides a "mock" web environment.
//RANDOM_PORT -> Loads the webserver application context and provide a "real" web environment. It will basically start the embedded server and listen to the random port that is available in the server which should be used for integration testing.
//DEFINED_PORT -> Loads the webserver application context and provide a real web environment. We need to define the port.
//NONE -> Loads the application context by using spring application, but does not provide any web environment.
