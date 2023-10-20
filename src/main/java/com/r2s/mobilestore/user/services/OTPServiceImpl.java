package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.Otp;
import com.r2s.mobilestore.user.repositories.OTPRepository;
import com.r2s.mobilestore.user.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Represents Otp Service
 * @author KhanhBD
 * @since 2023-10-03
 */
@Service
public class OTPServiceImpl implements OTPService {
    /**
     *  Set OTP_EXPIRATION_MINUTES = 3 minutes
     */
    private static final int OTP_EXPIRATION_MINUTES = 3;

    @Autowired
    private OTPRepository otpRepository;

    /**
     * This method is used to createOrUpdateOTP
     *
     * @return Otp code
     */
    @Override
    public Otp createOrUpdateOTP(String email) {
        Otp existingOtp = otpRepository.findByEmail(email);

        String otpCode = generateOTP();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, OTP_EXPIRATION_MINUTES);
        Date expirationTime = calendar.getTime();

        // If an Otp already exists for this email, update the Otp and expiration time
        if (existingOtp != null) {
            existingOtp.setOtpCode(otpCode);
            existingOtp.setExpirationTime(expirationTime);

            // Save Otp to database
            otpRepository.save(existingOtp);

            return existingOtp;
        }

        // If no Otp is found for this email, create a new Otp
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(new Date());
        otp.setExpirationTime(expirationTime);

        // Save Otp to database
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
        // Check if Otp is valid and not expired
        Otp otp = otpRepository.findByEmailAndOtpCode(email, otpCode);

        if (otp != null && otp.getExpirationTime().after(new Date())) {
            return true; // Otp Valid
        }

        return false; // Otp is invalid or has expired
    }

    /**
     * This method is used to generateOTP
     *
     * @return Otp code string
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
        Otp existingOtp = otpRepository.findByEmail(email);

        if (existingOtp != null) {
            otpRepository.delete(existingOtp);
        }
    }

    @Override
    @Transactional
    public void cleanupExpiredOTP() {
        Date currentTime = new Date();
        otpRepository.deleteByExpirationTimeBefore(currentTime);
    }
}
