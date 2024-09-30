package com.finzly.bbc.models.payments;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// PaymentDetail Class
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class PaymentDetail {
    @Id
    @GeneratedValue
    private UUID id;

    // Common fields if needed for all payment methods
}
