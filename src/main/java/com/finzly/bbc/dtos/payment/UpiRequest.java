package com.finzly.bbc.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpiRequest {
    private String upiId;
    private String pin;
    private String accountNo;
}


