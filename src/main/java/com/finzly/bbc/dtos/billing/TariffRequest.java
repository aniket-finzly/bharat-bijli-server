package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffRequest {

    private String connectionType;
    private Integer minUnits;
    private Integer maxUnits;
    private BigDecimal ratePerUnit;
    private String description;
}
