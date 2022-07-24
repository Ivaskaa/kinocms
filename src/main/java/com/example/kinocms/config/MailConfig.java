package com.example.kinocms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

//    @Value("${spring.mail.host}")
//    private String host;
//    @Value("${spring.mail.username}")
//    private String username;
//    @Value("${spring.mail.password}")
//    private String password;
//    @Value("${spring.mail.port}")
//    private int port;
//    @Value("${spring.mail.protocol}")
//    private String protocol;
//    @Value("${spring.mail.properties.mail.smtp.auth}")
//    private boolean auth;
//    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
//    private boolean enable;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("sazb.serg@gmail.com");
        mailSender.setPassword("gnnrrdsyzggoomdz");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        //mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}