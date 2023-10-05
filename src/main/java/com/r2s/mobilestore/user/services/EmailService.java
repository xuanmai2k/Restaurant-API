package com.r2s.mobilestore.user.services;

/**
 * Represents Email Service
 * @author KhanhBD
 * @since 2023-10-03
 */
public interface EmailService {

    /**
     * This method is used to sendEmail
     *
     */
    public void sendEmail(String to, String subject, String text);
}

