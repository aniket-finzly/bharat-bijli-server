package com.finzly.bbc.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private String employeeId;
    private String userId;
    private String designation;
    private Double salary;
}
