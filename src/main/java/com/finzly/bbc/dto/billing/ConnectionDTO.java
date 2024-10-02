package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
    private String id;
    private String customerId;
    private String connectionTypeId;
    private LocalDateTime startDate;
    private String status;
}

