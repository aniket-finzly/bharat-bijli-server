package com.finzly.bbc.dto.payment;

import lombok.Data;

@Data
public class UpiDTO {
    private String accountId;
    private String upiId;
    private String pin;
}




