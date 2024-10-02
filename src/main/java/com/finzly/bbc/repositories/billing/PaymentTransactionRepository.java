package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {
    List<PaymentTransaction> findByInvoiceId (String invoiceId);
}
