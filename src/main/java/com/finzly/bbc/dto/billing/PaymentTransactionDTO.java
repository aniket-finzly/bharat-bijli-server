package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDTO {
    private String id;
    private String invoiceId;
    private LocalDateTime paymentDate;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private String paymentStatus;
}
