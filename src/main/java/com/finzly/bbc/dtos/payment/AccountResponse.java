package com.finzly.bbc.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private String id;
    private String accountHolderName;
    private String accountHolderEmail;
    private String accountNo;
    private String bankName;
    private String ifsc;
    private Double balance;
    private LocalDateTime createdAt;
}
