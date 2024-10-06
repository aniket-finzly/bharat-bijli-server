package com.finzly.bbc.dtos.billing;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InvoiceRequest {
    private String customerId;
    private LocalDate dueDate;
    private LocalDate month;
    private Integer units;
}
