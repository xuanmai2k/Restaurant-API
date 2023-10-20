package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Represents Email Service
 * @author KhanhBD
 * @since 2023-10-03
 */
@Service
public class EmailServiceImpl implements EmailService {

    //Setting email config in application.yml, if not then error
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }
}
