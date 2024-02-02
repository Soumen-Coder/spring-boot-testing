package com.testing.base.springboot.service;

import com.testing.base.springboot.exception.EmployeeServiceException;
import com.testing.base.springboot.model.Employee;
import com.testing.base.springboot.repository.EmployeeRepository;
import com.testing.base.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//We need to tell Mockito that we are using Mockito annotations to mock the dependencies otherwise the tests will fail with NPE
//Because without informing Mockito, that we are using annotations to mock the dependencies, the dependencies are not mocked properly upon using annotations.
//We inform Mockito by @ExtentWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    //private EmployeeService employeeService; ->  this was used when we were using the setup method
    //But if we use the @InjectMocks annotation then, we have to provide the implementation class of it and not the interface
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    //Basically @InjectMocks annotation will first mock the employeeService dependency and, then it will inject another @mock dependency into it.
    //So whenever you want to inject one more dependency into another mocked dependency, then you can go ahead and use and @InjectMocks annotation.

    private Employee employee;
    @BeforeEach
    public void setup() {
        /*
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
        */
        employee = Employee.builder()
                .id(1L)
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();
    }

    //Junit for saveEmployee method operation
    @DisplayName("Junit test for saveEmployee operation in EmployeeService")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(1);
    }

    //Junit for saveEmployee method operation with exception being thrown from it
    @DisplayName("Junit test for saveEmployee operation which throws exception in EmployeeService")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour that we are going to test
        Assertions.assertThrows(EmployeeServiceException.class, () -> employeeService.saveEmployee(employee));

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //Junit for saveEmployee method operation with exception being thrown from it
    @DisplayName("Junit test for getAllEmployees operation in EmployeeService")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Micronaut")
                .lastName("latest")
                .email("micronaut.latest@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit for saveEmployee method operation (negative scenario)
    @DisplayName("Junit test for getAllEmployees operation in EmployeeService negative scenario")
    @Test
    public void givenEmptyList_whenGetAllEmployees_thenReturnEmptyList() {
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //Junit for getEmployeeById method operation
    @DisplayName("Junit test for getEmployeeById operation in EmployeeService")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        Employee retrievedEmployee = null;
        //given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        //when - action or behaviour that we are going to test
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(employee.getId());
        if(optionalEmployee.isPresent()){
            retrievedEmployee = optionalEmployee.get();
        }

        //then - verify the output
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getId()).isEqualTo(1);
    }

    //Junit for update employee operation
    @DisplayName("Junit test for updateEmployee operation in EmployeeService")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Apache");
        employee.setLastName("Maven");
        employee.setEmail("apache.maven@gmail.com");

        //when - action or behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualToIgnoringCase("apache.maven@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualToIgnoringCase("apache");
    }

    //Junit for delete employee operation
    @DisplayName("Junit test for deleteEmployee operation in EmployeeService")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId()); //to return void -> willDoNothing belongs to BDDMockito class, doNothing() belongs to Mockito

        //when - action or behaviour that we are going to test
        employeeService.deleteEmployeeById(employee.getId());

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}


//Class which we are going to test is called -> ClassUnderTest
//Methods of ClassUnderTest that we are going to test is called -> MethodUnderTest
//Mockito provides a static method called mock() Method. We can use Mockito class-provided static method mock() to create a mock object of a given class or an interface and this is the simplest way to mock an object.

//Second way is using add @Mock annotation. It is useful when we want to use mocked objects at the multiple places because we avoid calling mock Method multiple times.
//So we can use @Mock annotation to mock object at once. We don't have to call mock() Method again again in each a test case.
//@InjectMocks annotation -> When we want to inject a mocked object into another mocked object we can use @InjectMocks annotation.
//Like in EmployeeServiceTests class we can use @Mock for EmployeeRepository and @InjectMocks for EmployeeServiceImpl -> The injectMock will invoke the mocked repository as well along with the mocked EmployeeServiceImpl.
//So basically @InjectMocks annotation, it will first mock employee service impl class, and then it will inject employee repository because the employee repositories are already mocked using @Mock annotation.
//@InjectMocks annotation create the mock object of the class first, and then it will inject the mocks that are marked with annotation @Mock into it.
// Mockito library is shipped with a BDDMockito class which introduces BDD friendly APIs -> divided the unit test into three parts given/when/then.
//given(employeeRepository.findAll()).willReturn(List.of(employee1, employee2) -> we use given() and willReturn() methods to create a stub for this findAll() method to return the configured response.