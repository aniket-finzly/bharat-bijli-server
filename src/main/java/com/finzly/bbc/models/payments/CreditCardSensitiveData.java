package com.finzly.bbc.models.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// CreditCard Sensitive Data Class
@Entity
@Data
@NoArgsConstructor
public class CreditCardSensitiveData {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String encryptedCvv; // Encrypted CVV
}
