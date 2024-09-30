package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// DebitCard Repository
@Repository
public interface DebitCardRepository extends JpaRepository<DebitCard, UUID> {
    // You can define custom query methods here, if needed
}
