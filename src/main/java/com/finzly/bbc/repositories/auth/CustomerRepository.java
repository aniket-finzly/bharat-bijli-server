package com.finzly.bbc.repositories.auth;

import com.finzly.bbc.models.auth.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE " +
            "(:userId IS NULL OR c.user.id = :userId) AND " +
            "(:email IS NULL OR c.user.email = :email) AND " +
            "(:phoneNumber IS NULL OR c.user.phoneNumber = :phoneNumber) AND " +
            "(:isAdmin IS NULL OR c.user.isAdmin = :isAdmin)")
    List<Customer> searchCustomers (@Param("userId") String userId,
                                    @Param("email") String email,
                                    @Param("phoneNumber") String phoneNumber,
                                    @Param("isAdmin") Boolean isAdmin);
}
