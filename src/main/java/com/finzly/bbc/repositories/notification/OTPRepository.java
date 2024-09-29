package com.finzly.bbc.repositories.notification;

import com.finzly.bbc.models.notification.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
}
