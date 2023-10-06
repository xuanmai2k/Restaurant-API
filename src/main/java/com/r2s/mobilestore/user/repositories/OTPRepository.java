package com.r2s.mobilestore.user.repositories;

import com.r2s.mobilestore.user.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Represents otp repository
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Repository
public interface OTPRepository extends JpaRepository<Otp, Long> {
    Otp findByEmailAndOtpCode(String email, String otpCode);
    Otp findByEmail(String email);
    void deleteByExpirationTimeBefore(Date currentTime);
}
