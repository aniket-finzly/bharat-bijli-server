package com.finzly.bbc.models.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice; // Relationship with Invoice

    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff; // Relationship with Tariff

    @Column(nullable = false)
    private BigDecimal unitsConsumed;

    @Column(nullable = false)
    private BigDecimal chargeAmount;

    private String description;
}
