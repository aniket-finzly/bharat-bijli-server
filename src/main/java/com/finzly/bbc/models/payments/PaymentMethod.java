package com.finzly.bbc.models.payments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// PaymentMethod Class
@Entity
@Data
@NoArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String type; // e.g., UPI, CREDIT_CARD, DEBIT_CARD

    private LocalDateTime createdAt = LocalDateTime.now ();

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Reference to the account associated with this payment method
}
