package com.finzly.bbc.models.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String senderAccountId;

    @Column(nullable = false)
    private String receiverAccountId;

    @Column(nullable = false)
    private double amount; // Amount transferred

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // Type of transaction: CREDIT or DEBIT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType; // Type of payment: UPI, CREDIT_CARD, DEBIT_CARD

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime transactionDate;
}
