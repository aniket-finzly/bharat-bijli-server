package com.finzly.bbc.dtos.billing;

import com.finzly.bbc.constants.InvoiceTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {

    private String id;
    private String connectionId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String connectionType;
    private LocalDate month;
    private LocalDate dueDate;
    private String rateApplicable;
    private Integer units;
    private Double billAmount;
    private Double finalAmount;
    private InvoiceTransactionStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


