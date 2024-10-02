package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TariffRepository extends JpaRepository<Tariff, String> {
    List<Tariff> findByConnectionTypeId (String connectionTypeId);
}

