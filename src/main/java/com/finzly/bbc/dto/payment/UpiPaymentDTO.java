package com.finzly.bbc.dto.payment;

import lombok.Data;

@Data
public class UpiPaymentDTO {
    private String senderUpiId;
    private String receiverUpiId;
    private Long amount;
    private String senderPin;
}
