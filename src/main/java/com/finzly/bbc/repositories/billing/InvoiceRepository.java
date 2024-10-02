package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByConnectionId (String connectionId);

    List<Invoice> findByDueDateBefore (LocalDateTime date);
}
