package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    private String connectionId;
    private LocalDateTime billingPeriodStart;
    private BigDecimal totalUnits;
    private LocalDateTime dueDate;
}


