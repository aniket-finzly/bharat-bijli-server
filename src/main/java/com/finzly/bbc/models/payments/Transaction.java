package com.finzly.bbc.models.payments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Transaction Class
@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private UUID transactionUuid; // For exposing a unique identifier for transactions

    @NotNull
    @Positive
    private BigDecimal amount;

    private String status = "PENDING"; // e.g., PENDING, SUCCESS, FAILED

    private LocalDateTime createdAt = LocalDateTime.now ();

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Reference to the account related to this transaction

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod; // Reference to the payment method used for this transaction
}
