package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// CreditCard Repository
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {
    // You can define custom query methods here, if needed
}
