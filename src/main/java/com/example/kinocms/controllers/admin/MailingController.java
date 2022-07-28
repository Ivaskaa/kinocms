package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.service.MailService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")  //для всього NewsController
public class MailingController {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private MailService mailService;

    @GetMapping("/mailing/getUsers")
    @ResponseBody   // json
    public String getUsers () throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // для роботи з JSON
        List<User> users = userService.findUserByWriteEmail();
        return mapper.writeValueAsString(users);
    }

    @GetMapping("/mailing")
    public String adminMailing(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("subject", new String());
        model.addAttribute("message", new String());
        return "admin/mailing/mailing";
    }

    @PostMapping("/mailing")
    public String adminMailingSend(
            Model model,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam(value = "mailingUser", required = false) User userForm // required true - форма обовязково має щось відправити
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("subject", subject);
        model.addAttribute("message", message);
//        if(userForm != null){
//            model.addAttribute("mailingUser", userForm);
//        }

        // validation
        String subjectValidation = mailService.subjectValidation(subject);
        if(subjectValidation != null){  // filmService validation
            model.addAttribute("subjectValidation", subjectValidation);
            return "admin/mailing/mailing";
        }
        String messageValidation = mailService.messageValidation(message);
        if(messageValidation != null){  // filmService validation
            model.addAttribute("messageValidation", messageValidation);
            return "admin/mailing/mailing";
        }

        // send
        if(userForm == null){
            List<User> users = userService.findUserByWriteEmail();
            for(User u : users){
                mailService.send(u.getEmail(), subject, message);
            }
        }else {
            mailService.send(userForm.getEmail(), subject, message);
        }
        return "redirect:/admin/mailing";     // перенаправляє на /admin/films
    }


}
