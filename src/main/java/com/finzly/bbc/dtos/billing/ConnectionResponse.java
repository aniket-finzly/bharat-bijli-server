package com.finzly.bbc.dtos.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionResponse {
    private String id;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String connectionTypeId;
    private String connectionTypeName;
    private LocalDateTime startDate;
    private String status;
}
