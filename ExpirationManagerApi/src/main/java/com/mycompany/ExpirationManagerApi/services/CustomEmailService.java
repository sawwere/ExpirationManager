package com.mycompany.ExpirationManagerApi.services;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CustomEmailService{
    private static final Logger logger =
            Logger.getLogger(CustomEmailService.class.getName());

    private final JavaMailSender emailSender;

    public void sendSimpleEmail(String toAddress, String subject, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toAddress);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            emailSender.send(simpleMailMessage);
        } catch (MailException mailException) {
            logger.log(Level.SEVERE, String.format("Error while sending email %s", mailException.getMessage()));
        }
    }
}
