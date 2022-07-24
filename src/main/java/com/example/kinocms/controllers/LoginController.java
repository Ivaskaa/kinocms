package com.example.kinocms.controllers;

import com.example.kinocms.entities.User;
import com.example.kinocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;



    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userForm", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginButton(
            @ModelAttribute("userForm") User userForm,
            Model model)
    {
        System.out.println("loginButton");
        if(userForm.getUsername().isEmpty()){
            model.addAttribute("msg", "Login is empty");
            System.out.println("Login is empty");
            return "login";
        }
        if(userForm.getPassword().isEmpty()){
            model.addAttribute("msg", "Password is empty");
            System.out.println("Password is empty");
            return "login";
        }
        if(!userService.loadUserByUsername(userForm.getUsername()).equals(userForm.getUsername())){
            model.addAttribute("msg", "Entered login is not registered");
            System.out.println("Entered login is not registered");
            return "login";
        }

        return "user/user";
    }

//    @PostMapping("/login")
//    public String addUser(
//            @ModelAttribute("userForm") User userForm,
//            BindingResult bindingResult,
//            Model model
//    ) {
//        if(userForm == null){
//            return "registration";
//        }
//        User user = (User) userService.loadUserByUsername(userForm.getUsername());
//        if(user == null){
//            return "registration";
//        }
//        if(bindingResult.hasErrors()) {
//            return "registration";
//        }
//        if(!userForm.getPassword().equals(user.getPassword())){
//            return "registration";
//        }
//
//        return "redirect:/";
//    }
}
