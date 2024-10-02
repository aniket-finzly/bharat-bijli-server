package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffSlabDTO {
    private String id;
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal ratePerUnit;
    private String tariffId;
}
