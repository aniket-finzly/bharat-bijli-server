package com.finzly.bbc.repositories.payment;

import com.finzly.bbc.models.payment.Upi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpiRepository extends JpaRepository<Upi, String> {
    Upi findByUpiId (String upiId);
}
