package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String> {
    // Change to reference customer.customerId
    List<Connection> findByCustomer_CustomerId (String customerId);

    List<Connection> findByStatus (ConnectionStatus status);
}

