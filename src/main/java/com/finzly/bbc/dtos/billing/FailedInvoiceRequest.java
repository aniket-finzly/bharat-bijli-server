package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedInvoiceRequest {
    private InvoiceRequest invoiceRequest; // The original request that failed
    private String errorMessage;            // Reason for the failure
}
