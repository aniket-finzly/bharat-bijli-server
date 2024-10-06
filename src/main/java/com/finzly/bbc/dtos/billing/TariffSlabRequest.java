package com.finzly.bbc.dtos.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TariffSlabRequest {
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal ratePerUnit;
    private String tariffId;
}

