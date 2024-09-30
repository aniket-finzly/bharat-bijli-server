package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// PaymentDetail Repository
@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, UUID> {
    // This interface may not require custom methods unless needed
}
