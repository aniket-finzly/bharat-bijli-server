package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("SELECT i FROM Invoice i WHERE (:connectionId IS NULL OR i.connection.id = :connectionId) " +
            "AND (:dueDate IS NULL OR i.dueDate = :dueDate)")
    Page<Invoice> searchInvoices (@Param("connectionId") String connectionId,
                                  @Param("dueDate") LocalDateTime dueDate,
                                  Pageable pageable);

    boolean existsByConnectionAndMonth (Connection connection, LocalDate month);

    Collection<Invoice> findByConnection_Customer (Customer customer);
}
