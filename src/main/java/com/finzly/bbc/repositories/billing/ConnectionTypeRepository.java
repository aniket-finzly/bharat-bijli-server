package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.ConnectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionTypeRepository extends JpaRepository<ConnectionType, String> {
    // Custom query methods can be defined here if needed
}
