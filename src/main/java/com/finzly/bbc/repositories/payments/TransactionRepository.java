package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// Transaction Repository
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    // You can define custom query methods here, if needed
}
