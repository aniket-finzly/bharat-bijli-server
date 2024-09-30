package com.finzly.bbc.models.payments;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// CreditCard Class with Tokenization
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class CreditCard extends PaymentDetail {
    @NotBlank
    private String tokenizedNumber; // Tokenized credit card number

    @NotBlank
    private String expiry;

    @NotBlank
    private String holderName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensitive_data_id", referencedColumnName = "id")
    private CreditCardSensitiveData sensitiveData; // Reference to sensitive data
}
