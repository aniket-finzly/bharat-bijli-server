package com.finzly.bbc.models.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String senderAccountId; // ID of the sender's account

    @Column(nullable = false)
    private String receiverAccountId; // ID of the receiver's account

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
    private PaymentStatus status; // Status of the transaction

    @Column(nullable = false)
    private LocalDateTime transactionDate; // Timestamp of the transaction

    @PrePersist
    protected void onCreate () {
        this.transactionDate = LocalDateTime.now (); // Set the current time when creating a transaction
        this.status = PaymentStatus.PENDING; // Initial status
    }
}
