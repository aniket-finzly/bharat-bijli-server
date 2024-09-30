package com.finzly.bbc.repositories.payments;

import com.finzly.bbc.models.payments.UPISensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// UPI Sensitive Data Repository
@Repository
public interface UPISensitiveDataRepository extends JpaRepository<UPISensitiveData, UUID> {
    // You can define custom query methods here, if needed
}
