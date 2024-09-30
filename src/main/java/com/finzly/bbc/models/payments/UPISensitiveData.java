package com.finzly.bbc.models.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// UPI Sensitive Data Class
@Entity
@Data
@NoArgsConstructor
public class UPISensitiveData {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String encryptedPin; // Store encrypted UPI PIN
}
