package com.finzly.bbc.dtos.billing;

import com.finzly.bbc.models.billing.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private String id;
    private String connectionId;
    private LocalDateTime billingPeriodStart;
    private LocalDateTime billingPeriodEnd;
    private BigDecimal totalUnits;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
