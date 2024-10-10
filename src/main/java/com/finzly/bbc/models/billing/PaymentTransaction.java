package com.finzly.bbc.models.billing;

import com.finzly.bbc.constants.InvoiceTransactionStatus;
import com.finzly.bbc.constants.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice; // Relationship with Invoice

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Double paymentAmount;

    @Column(nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private InvoiceTransactionStatus paymentStatus;
}
