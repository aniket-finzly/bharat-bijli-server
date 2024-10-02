package com.finzly.bbc.repositories.billing;

import com.finzly.bbc.models.billing.TariffSlab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffSlabRepository extends JpaRepository<TariffSlab, String> {
    List<TariffSlab> findByTariffId (String tariffId);
}
