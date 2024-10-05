package com.finzly.bbc.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedUserCustomerRequest {
    private UserCustomerRequest request; // The original request
    private String errorMessage; // Error message for the failure
}
