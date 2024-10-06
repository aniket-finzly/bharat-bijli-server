package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {
    private String customerId;
    private String connectionTypeId;
    private String status;
}


