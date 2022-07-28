package com.example.kinocms;

//import com.example.kinocms.service.MailSender;

import com.example.kinocms.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KinocmsApplication{
	@Autowired
	private MailService mailService;

	public static void main(String[] args) {
		SpringApplication.run(KinocmsApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail(){
//		mailSender.send("cmskino@gmail.com", "subject", "ок");
//	}
}
