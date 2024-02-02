package com.testing.base.springboot.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

//Singleton pattern is being used here to be reused by other integration test classes for creating docker container and deploying mysql image in it.
//Here, @Container or @TestContainer is not used because we have manually started the container and don't want @Testcontainer to manage the lifecycle of the container.
//This abstract class can be extended by subclasses as and when the container is required to run tests.
public abstract class AbstractionContainerBaseTest {
    static final MySQLContainer MY_SQL_CONTAINER;
    
    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withDatabaseName("use_your_database_name_don't_hardcode_use_property_file")
                .withUsername("an_employee_don't_hardcode_use_property_file")
                .withPassword("needs_to_be_encrypted_please__don't_hardcode_use_property_file");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

}
