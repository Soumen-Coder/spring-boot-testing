package com.testing.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootReactiveTestingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactiveTestingApplication.class, args);
    }
}

//Spring WebFlux is a reactive web framework introduced in a spring framework 5.
//Spring WebFlux is designed to provide the reactive programming model for building web applications and rest APIs using spring.
//Spring WebFlux is built on top of Project Reactor, which is a popular reactive programming library for JVM.
//Spring WebFlux is supported by Tomcat, Jetty, Netty and Undertow servers.
//In order to connect the reactive web applications with the database, we have to use the reactive database drivers.
//Reactive MongoDB Driver provides a reactive support for MongoDB database.
//Reactive Redis driver provides a reactive support for Redis. Redis is a in-memory key value store.
//Reactive Cassandra Driver provides support for Cassandra NoSQL database.
//These are the popular supported reactive drivers in a Spring WebFlux.
//Can we support MySQL Server or PostgreSQL database or Oracle database(RDBMS) with this Spring WebFlux application?
//NO, because the JDBC driver with respect to these databases does not support the reactive programming.
//There are two ways we can develop the Spring WebFlux application.
//First, is using traditional annotation based model with @Controller and @RequestMapping annotations.
//Next, a new functional style model based on Java 8 Lambda Expressions for routing and handling requests.