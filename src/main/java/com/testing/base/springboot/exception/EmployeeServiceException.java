package com.testing.base.springboot.exception;

public class EmployeeServiceException extends RuntimeException {
    EmployeeServiceException() {
        super();
    }

    public EmployeeServiceException(String message) {
        super(message);
    }

    EmployeeServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
