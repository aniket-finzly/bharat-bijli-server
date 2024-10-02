package com.finzly.bbc.models.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tariff_slab")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffSlab {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer minUnits;

    @Column(nullable = false)
    private Integer maxUnits;

    @Column(nullable = false)
    private BigDecimal ratePerUnit;

    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

}
