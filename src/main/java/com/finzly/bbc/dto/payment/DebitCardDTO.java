package com.finzly.bbc.dto.payment;

import lombok.Data;

@Data
public class DebitCardDTO {
    private String accountId;
    private String pin;
}
