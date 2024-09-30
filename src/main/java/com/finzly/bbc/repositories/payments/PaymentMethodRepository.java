package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// PaymentMethod Repository
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    // You can define custom query methods here, if needed
}
