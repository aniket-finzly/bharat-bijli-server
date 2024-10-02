package com.finzly.bbc.models.billing;

import com.finzly.bbc.models.auth.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "connection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Relationship with Customer

    @ManyToOne
    @JoinColumn(name = "connection_type_id", nullable = false)
    private ConnectionType connectionType; // Relationship with ConnectionType

    @Column(nullable = false)
    private LocalDateTime startDate; // Changed to LocalDateTime

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices; // List of invoices for this connection
}
