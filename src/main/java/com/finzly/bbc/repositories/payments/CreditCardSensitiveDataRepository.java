package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.CreditCardSensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// Credit Card Sensitive Data Repository
@Repository
public interface CreditCardSensitiveDataRepository extends JpaRepository<CreditCardSensitiveData, UUID> {
    // You can define custom query methods here, if needed
}
