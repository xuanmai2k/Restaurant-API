package com.r2s.mobilestore.user.repositories;

import com.r2s.mobilestore.user.entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Represents otp repository
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findByEmailAndOtpCode(String email, String otpCode);
    OTP findByEmail(String email);
    void deleteByExpirationTimeBefore(Date currentTime);
}
