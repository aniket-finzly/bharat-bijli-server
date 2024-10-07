package com.finzly.bbc.repositories.payment;

import com.finzly.bbc.models.payment.Upi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpiRepository extends JpaRepository<Upi, String> {

    // Method using Spring Data JPA query derivation
    Upi findByUpiId (String upiId);

    // Custom query to find UPI by UPI ID
    @Query("SELECT u FROM Upi u WHERE u.upiId = :upiId")
    Upi findByUpiIdCustom (@Param("upiId") String upiId);
}
