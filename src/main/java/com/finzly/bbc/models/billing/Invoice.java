package com.finzly.bbc.models.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "connection_id", nullable = false)
    private Connection connection; // Relationship with Connection

    @Column(nullable = false)
    private LocalDateTime billingPeriodStart; // Changed to LocalDateTime

    @Column(nullable = false)
    private LocalDateTime billingPeriodEnd; // Changed to LocalDateTime

    @Column(nullable = false)
    private BigDecimal totalUnits;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt = LocalDateTime.now (); // Set to current date and time
    private LocalDateTime updatedAt = LocalDateTime.now (); // Set to current date and time
}
