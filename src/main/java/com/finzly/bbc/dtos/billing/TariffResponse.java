package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffResponse {

    private String id;
    private String connectionTypeId;
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal ratePerUnit;
    private String description;
}
