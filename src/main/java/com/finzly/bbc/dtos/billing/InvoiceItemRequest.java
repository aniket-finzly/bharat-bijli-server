package com.finzly.bbc.dtos.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemRequest {
    private String invoiceId;

    private String tariffId;

    private BigDecimal unitsConsumed;

    private BigDecimal chargeAmount;

    private String description;
}
