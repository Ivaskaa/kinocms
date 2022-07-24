package com.example.kinocms;

import com.example.kinocms.service.MailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KinocmsApplicationTests {

	@Autowired
	private MailSender mailSender;
	@Test
	void contextLoads() {
		mailSender.send("romagagarin422@gmail.com", "message", "message");
	}

}
