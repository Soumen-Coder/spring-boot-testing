package com.testing.base.springboot.repository;

import com.testing.base.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EmployeeRepositoryTests {
    private final EmployeeRepository employeeRepository;
    private Employee employee;

    @Autowired
    public EmployeeRepositoryTests(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @BeforeEach
    public void setup() {
         employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();
    }

    //Junit for save employee operation
    @DisplayName("Junit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit for findAll employee operation
    @DisplayName("Junit test for findAll employee operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        //given - precondition or setup
        /*
        Employee employee1 = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();
                */

        Employee employee2 = Employee.builder()
                .firstName("Java")
                .lastName("17")
                .email("java.17@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit for findById employee operation
    @DisplayName("Junit test for findById employee operation")
    @Test
    public void givenEmployee_whenFindById_thenReturnEmployee() {
        Employee savedEmployee = null;

        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        if(optionalEmployee.isPresent()){
             savedEmployee = optionalEmployee.get();
        }

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }

    //Junit for findByEmail employee operation
    @DisplayName("Junit test for findByEmail employee operation")
    @Test
    public void givenEmployee_whenFindByEmail_thenReturnEmployee() {
        Employee savedEmployee = null;

        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(optionalEmployee.isPresent()){
            savedEmployee = optionalEmployee.get();
        }

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    //Junit for delete employee operation
    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(optionalEmployee).isEmpty();
    }

    //Junit for deleteById employee operation
    @DisplayName("Junit test for deleteById employee operation")
    @Test
    public void givenEmployee_whenDeleteById_thenRemoveEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        employeeRepository.deleteById(employee.getId());

        //then - verify the output
        assertThat(employeeRepository.findById(employee.getId()).isEmpty()).isTrue();
    }

    //Junit for update employee operation
        @DisplayName("Junit test for update employee")
        @Test
        public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
            Employee savedEmployee = null;

            //given - precondition or setup
            /*Employee employee = Employee.builder()
                    .firstName("Spring")
                    .lastName("Boot")
                    .email("spring.boot@gmail.com")
                    .build();*/
            employeeRepository.save(employee);

            //when - action or behaviour that we are going to test
            Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
            if(optionalEmployee.isPresent()){
                savedEmployee = optionalEmployee.get();
            }
            assert savedEmployee != null;
            savedEmployee.setFirstName("Apache");
            savedEmployee.setLastName("Maven");
            savedEmployee.setEmail("apache.maven@gmail.com");

            //then - verify the output
            assertThat(savedEmployee.getFirstName()).isEqualToIgnoringCase("apache");
            assertThat(savedEmployee.getLastName()).isEqualToIgnoringCase("maven");
            assertThat(savedEmployee.getEmail()).isEqualToIgnoringCase("apache.maven@gmail.com");
        }

    @DisplayName("Junit test for findByFirstNameAndLastNameUsingJPQLIndex employee operation")
    @Test
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastNameUsingJPQLIndex_thenReturnEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        String firstName = "Spring";
        String lastName = "Boot";

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameUsingJPQLIndex(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for findByFirstNameAndLastNameUsingJPQLNamedParam employee operation")
    @Test
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastNameUsingJPQLNamedParam_thenReturnEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        String firstName = "Spring";
        String lastName = "Boot";

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameUsingJPQLNamedParam(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for findByFirstNameAndLastNameUsingNativeSQLIndexParam employee operation")
    @Test
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastNameUsingNativeSQLIndexParam_thenReturnEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        //String firstName = "Spring";
        //String lastName = "Boot";

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameUsingNativeQueryIndexParam(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for findByFirstNameAndLastNameNativeSQLNamedParam employee operation")
    @Test
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastNameUsingNativeSQLNamedParam_thenReturnEmployee() {
        //given - precondition or setup
        /*Employee employee = Employee.builder()
                .firstName("Spring")
                .lastName("Boot")
                .email("spring.boot@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        //String firstName = "Spring";
        //String lastName = "Boot";

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameUsingNativeQueryNamedParam(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}


