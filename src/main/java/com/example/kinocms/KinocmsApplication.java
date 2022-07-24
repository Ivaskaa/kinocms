package com.example.kinocms;

//import com.example.kinocms.service.MailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class KinocmsApplication{

	@Autowired
	private JavaMailSender mailSender;

	public static void main(String[] args) {
		SpringApplication.run(KinocmsApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendMail(){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("cmskino@gmail.com");
		mailMessage.setTo("cmskino@gmail.com");
		mailMessage.setSubject("subject");
		mailMessage.setText("message");
		mailSender.send(mailMessage);
	}

}
