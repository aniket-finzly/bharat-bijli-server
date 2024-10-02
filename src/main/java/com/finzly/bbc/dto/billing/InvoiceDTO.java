package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String id;
    private String connectionId;
    private LocalDateTime billingPeriodStart;
    private LocalDateTime billingPeriodEnd;
    private BigDecimal totalUnits;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private LocalDateTime dueDate;
}
