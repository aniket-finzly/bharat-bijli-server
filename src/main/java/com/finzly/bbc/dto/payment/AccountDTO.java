package com.finzly.bbc.dto.payment;

import lombok.Data;

@Data
public class AccountDTO {
    private String accountNo;
    private String bankName;
    private String ifsc;
    private Long accountBalance;
}
