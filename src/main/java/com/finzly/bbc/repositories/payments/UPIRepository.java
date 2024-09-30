package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.UPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// UPI Repository
@Repository
public interface UPIRepository extends JpaRepository<UPI, UUID> {
    // You can define custom query methods here, if needed
}
