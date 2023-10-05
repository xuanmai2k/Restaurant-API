package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.OTP;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents OTP Service
 * @author KhanhBD
 * @since 2023-10-03
 */
public interface OTPService {

    /**
     * This method is used to createOrUpdateOTP
     *
     * @return OTP code
     */
    public OTP createOrUpdateOTP(String email);

    /**
     * This method is used to isOTPValid
     *
     * @return true or false
     */
    public boolean isOTPValid(String email, String otpCode);

    /**
     * This method is used to generateOTP
     *
     * @return OTP code string
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

