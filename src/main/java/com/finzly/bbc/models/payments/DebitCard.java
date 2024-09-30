package com.finzly.bbc.models.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

// DebitCard Class
@Entity
@Data
@NoArgsConstructor
public class DebitCard extends PaymentDetail {
    @NotBlank
    private String number; // Consider tokenization here as well

    @NotBlank
    private String expiry;

    @NotBlank
    private String holderName;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Reference to the account associated with this debit card
}
