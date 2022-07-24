package com.example.kinocms;

//import com.example.kinocms.service.MailSender;

import com.example.kinocms.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class KinocmsApplication{

	private MailSender mailSender;


	public static void main(String[] args) {
		SpringApplication.run(KinocmsApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendMail(){
		mailSender.send("cmskino@gmail.com", "subject", "message");
	}

}
