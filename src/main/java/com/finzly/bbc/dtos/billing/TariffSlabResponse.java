package com.finzly.bbc.dtos.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TariffSlabResponse {
    private String id;
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal ratePerUnit;
    private String tariffId;
}
