package com.finzly.bbc.dtos.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TariffResponse {
    private String id;
    private String connectionTypeId;
    private String description;
}

