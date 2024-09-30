package com.finzly.bbc.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmployeeSearchDTO {
    private String designation;
    private Double minSalary;
    private Double maxSalary;
    private String userId;
    private String email;
    private String phoneNumber;
    private Boolean isAdmin;
}
