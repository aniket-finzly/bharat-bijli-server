package com.finzly.bbc.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String designation;
    private Double salary;
}
