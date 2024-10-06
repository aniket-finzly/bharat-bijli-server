package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkInvoiceResponse {
    private List<InvoiceResponse> successfulResponses;
    private List<FailedInvoiceRequest> failedRequests;
}


