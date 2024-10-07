package com.finzly.bbc.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String bankName;
    private String ifsc;
    private Double balance;
    private String userId;
}


