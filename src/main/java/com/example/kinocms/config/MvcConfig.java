package com.example.kinocms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration  // обовязково для MvcConfig
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    public void addViewController(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")                    // каждое обращение к серверу по пути img
                .addResourceLocations("file:///" + uploadPath + "/img/");          // перенаправляется на  file:// + uploadPath + /
        registry.addResourceHandler("/plugins/**")
                .addResourceLocations("classpath:/static/plugins/");
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("classpath:/static/dist/");
    }
}
