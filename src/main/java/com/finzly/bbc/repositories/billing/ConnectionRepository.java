package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.billing.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String> {

    @Query("SELECT c FROM Connection c " +
            "WHERE (:customerId IS NULL OR c.customer.id = :customerId) " +
            "AND (:connectionTypeId IS NULL OR c.connectionType.id = :connectionTypeId) " +
            "AND (:status IS NULL OR c.status = :status)")
    Page<Connection> searchConnections (
            @Param("customerId") String customerId,
            @Param("connectionTypeId") String connectionTypeId,
            @Param("status") String status,
            Pageable pageable);

    Optional<Connection> findByCustomer (Customer customer);
}
