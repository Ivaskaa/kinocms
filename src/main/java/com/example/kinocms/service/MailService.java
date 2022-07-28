package com.example.kinocms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Service
public class MailService {
    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        emailSender.send(mailMessage);
    }
    public String subjectValidation(String subject) {
        if(subject.equals("")){
            return "Subject shouldn't be empty";
        }
        if(subject.length() > 255){
            return "Subject must be less than 255 characters";
        }
        return null;
    }

    public String messageValidation(String message) {
        if(message.equals("")){
            return "Message shouldn't be empty";
        }
        if(message.length() > 255){
            return "Message must be less than 255 characters";
        }
        return null;
    }
}
