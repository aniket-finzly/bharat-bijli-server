package com.finzly.bbc.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayByUpi {
    private String senderUpiId;
    private String receiverUpiId;
    private String senderPin;
    private double amount;
}
