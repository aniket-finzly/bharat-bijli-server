package com.finzly.bbc.dtos.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemResponse {
    private String id;
    private String invoiceId;
    private String tariffId;
    private BigDecimal unitsConsumed;
    private BigDecimal chargeAmount;
    private String description;
}


