package com.finzly.bbc.models.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "tariff",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"connection_type_id", "minUnits", "maxUnits"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "connection_type_id", nullable = false)
    private ConnectionType connectionType;

    @Column(nullable = false)
    private Integer minUnits;

    @Column(nullable = false)
    private Integer maxUnits;

    @Column(nullable = false)
    private Double ratePerUnit;
}
