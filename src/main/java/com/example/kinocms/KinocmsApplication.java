package com.example.kinocms;

//import com.example.kinocms.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.security.servlet.ApplicationContextRequestMatcher;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class KinocmsApplication{

//	@Autowired
//	private MailSender mailSender;

	public static void main(String[] args) {
		SpringApplication.run(KinocmsApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail(){
//		mailSender.send("ivansha200@gmail.com", "hello", "world");
//	}

}
