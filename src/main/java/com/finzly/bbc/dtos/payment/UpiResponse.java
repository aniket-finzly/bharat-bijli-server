package com.finzly.bbc.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpiResponse {
    private String id;
    private String upiId;
    private String accountNo;
    private String bankName;
    private String ifsc;
    private Double balance;
}



