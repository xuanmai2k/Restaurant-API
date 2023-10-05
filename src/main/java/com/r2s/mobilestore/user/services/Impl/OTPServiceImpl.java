package com.r2s.mobilestore.user.services.Impl;

import com.r2s.mobilestore.user.entities.OTP;
import com.r2s.mobilestore.user.repositories.OTPRepository;
import com.r2s.mobilestore.user.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Represents OTP Service
 * @author KhanhBD
 * @since 2023-10-03
 */
@Service
public class OTPServiceImpl implements OTPService {
    /**
     *  Set OTP_EXPIRATION_MINUTES = 1 minutes
     */
    private static final int OTP_EXPIRATION_MINUTES = 1;

    @Autowired
    private OTPRepository otpRepository;

    /**
     * This method is used to createOrUpdateOTP
     *
     * @return OTP code
     */
    @Override
    public OTP createOrUpdateOTP(String email) {
        OTP existingOTP = otpRepository.findByEmail(email);

        String otpCode = generateOTP();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, OTP_EXPIRATION_MINUTES);
        Date expirationTime = calendar.getTime();

        // If an OTP already exists for this email, update the OTP and expiration time
        if (existingOTP != null) {
            existingOTP.setOtpCode(otpCode);
            existingOTP.setExpirationTime(expirationTime);

            // Save OTP to database
            otpRepository.save(existingOTP);

            return existingOTP;
        }

        // If no OTP is found for this email, create a new OTP
        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(new Date());
        otp.setExpirationTime(expirationTime);

        // Save OTP to database
        otpRepository.save(otp);

        return otp;

    }

    /**
     * This method is used to isOTPValid
     *
     * @return true or false
     */
    @Override
    public boolean isOTPValid(String email, String otpCode) {
        // Check if OTP is valid and not expired
        OTP otp = otpRepository.findByEmailAndOtpCode(email, otpCode);

        if (otp != null && otp.getExpirationTime().after(new Date())) {
            return true; // OTP Valid
        }

        return false; // OTP is invalid or has expired
    }

    /**
     * This method is used to generateOTP
     *
     * @return OTP code string
     */
    @Override
    public String generateOTP() {
        Random random = new Random();
        int otpLength = 4;
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * This method is used to deleteOTP
     *
     */
    @Override
    public void deleteOTP(String email) {
        OTP existingOTP = otpRepository.findByEmail(email);

        if (existingOTP != null) {
            otpRepository.delete(existingOTP);
        }
    }

    @Override
    @Transactional
    public void cleanupExpiredOTP() {
        Date currentTime = new Date();
        otpRepository.deleteByExpirationTimeBefore(currentTime);
    }
}
