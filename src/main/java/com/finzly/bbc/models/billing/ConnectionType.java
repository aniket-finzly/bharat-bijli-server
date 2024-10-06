package com.finzly.bbc.models.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "connection_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionType {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String typeName;

    private String description;

    @OneToMany(mappedBy = "connectionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Connection> connections;
}

