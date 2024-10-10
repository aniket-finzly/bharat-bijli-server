package com.finzly.bbc.models.billing;

import com.finzly.bbc.constants.ConnectionStatus;
import com.finzly.bbc.models.auth.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "connection_type_id", nullable = false)
    private ConnectionType connectionType;

    @CreationTimestamp
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices; // List of invoices for this connection
}
