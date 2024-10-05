package com.finzly.bbc.dtos.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BulkUserCustomerResponse {
    private List<UserCustomerResponse> successfulResponses; // List of successful responses
    private List<FailedUserCustomerRequest> failedRequests; // List of failed requests
}



