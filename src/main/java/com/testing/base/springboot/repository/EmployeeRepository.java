package com.testing.base.springboot.repository;

import com.testing.base.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    //Define custom query using JPQL with index params
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByFirstNameAndLastNameUsingJPQLIndex(String firstName, String lastName);

    //Define custom query using JPQL with named parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByFirstNameAndLastNameUsingJPQLNamedParam(@Param("firstName") String firstName, @Param("lastName")String lastName);

    //Define custom query using Native SQL with index param
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByFirstNameAndLastNameUsingNativeQueryIndexParam(String firstName, String lastName);

    //Define custom query using Native SQL with named parameter
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
    Employee findByFirstNameAndLastNameUsingNativeQueryNamedParam(@Param("firstName") String firstName, @Param("lastName")String lastName);


}

//Used Long Datatype for the ID type in generics because we need to use wrapper class in it and not primitives.
//JpaRepository is an interface and its implementation class is SimpleJpaRepository which has @Repository annotation within it and all its method are annotated with @Transactional, hence we don't need to annotate our EmployeeRepository class with @Repository again

//JUnit test case in a BDD style -> given(preconditions), when(action, behaviour, actual testing), then(verify output, compare)
//Example : givenEmployeeObject -> whenSave -> thenReturnSavedEmployee
//JUnit test case is divided into three parts given/when/then.
//given -> we write the precondition or the setup that needed to test the actual behavior.
//When -> In order to test the actual behavior that is save() method, we need an employee object which we are going to set up in the given section.
//In "when" section, we do the exact testing, that is the behavior testing. Basically, this is the action that we are able to perform.
//Then section -> we verify the output, output of the "when" section.
//We basically write the assert statement to verify the output comparing the actual with expected result.
//assertThat() is basically from the AssertJ library.


//Spring Boot provides @DataJpaTest annotation to test only the persistence layer components or repository layer components.
//@DataJpaTest annotation will only load the spring data JPA components. It doesn't load the service layer components or controller layer components because we are testing only the repository layer, so it makes sense to only load the repository components.
//@DataJpaTest annotation will automatically configure in-memory embedded database for testing purposes.
//In order to test the repository layer, we don't have to connect to the real database like MySQL database or PostgresSQL database right. We are going to mock the data, so it makes sense to, you know, use in-memory and database for testing purposes.

//The @DataJpaTest annotation does not load others spring beans like @Controller, @Services, @Bean etc. into application context.
//Basically @DataJpaTest annotation by default, scans @Entity classes and configure spring data jpa repositories annotated with @Repository annotations.

//By default, test cases that are annotated with @DataJpaTest annotation are transactional and rollback at the end of each test.
//In order to disable the rollback, you can add @RollBackTest annotation on top of each test case.
//@DataJpaTest annotation is used to test JPA related components. For example DataStore(part of JDBC Driver), JdbcTemplate(part of Spring JDBC), EntityManager(part of Spring JPA), UserRepository(part of Spring Data JPA) classes.
