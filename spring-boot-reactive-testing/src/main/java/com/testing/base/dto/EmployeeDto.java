package com.testing.base.dto;

import lombok.*;

//To transfer the data between client and server
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
