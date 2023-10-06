package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.Otp;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents Otp Service
 * @author KhanhBD
 * @since 2023-10-03
 */
public interface OTPService {

    /**
     * This method is used to createOrUpdateOTP
     *
     * @return Otp code
     */
    public Otp createOrUpdateOTP(String email);

    /**
     * This method is used to isOTPValid
     *
     * @return true or false
     */
    public boolean isOTPValid(String email, String otpCode);

    /**
     * This method is used to generateOTP
     *
     * @return Otp code string
     */
    public String generateOTP();

    /**
     * This method is used to deleteOTP
     *
     */
    public void deleteOTP(String email);

    @Transactional
    public void cleanupExpiredOTP();
}

