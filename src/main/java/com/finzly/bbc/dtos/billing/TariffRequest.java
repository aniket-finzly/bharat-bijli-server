package com.finzly.bbc.dtos.billing;

import lombok.Data;

@Data
public class TariffRequest {
    private String connectionTypeId;

    private String description;
}


