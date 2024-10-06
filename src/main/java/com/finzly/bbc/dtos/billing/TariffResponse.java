package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffResponse {

    private String id;
    private String connectionTypeId;
    private Integer minUnits;
    private Integer maxUnits;
    private Double ratePerUnit;
}
