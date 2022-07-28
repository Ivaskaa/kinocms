package com.example.kinocms;

import com.example.kinocms.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KinocmsApplicationTests {

	@Autowired
	private MailService mailService;
	@Test
	void contextLoads() {
		mailService.send("romagagarin422@gmail.com", "message", "message");
	}

}
