package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    private String id;
    private String invoiceId;
    private String tariffId;
    private BigDecimal unitsConsumed;
    private BigDecimal chargeAmount;
    private String description;
}
