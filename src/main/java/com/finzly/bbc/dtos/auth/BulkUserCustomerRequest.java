package com.finzly.bbc.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BulkUserCustomerRequest {
    private List<UserCustomerRequest> userCustomers;
}
