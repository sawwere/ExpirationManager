package com.mycompany.ExpirationManagerApi.services;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomEmailService{

    private final JavaMailSender emailSender;

    public void sendSimpleEmail(String toAddress, String subject, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toAddress);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            emailSender.send(simpleMailMessage);
        } catch (MailException mailException) {
            System.out.println(String.format("Error while sending out email..%s", mailException.getStackTrace()));
        }
    }
}
