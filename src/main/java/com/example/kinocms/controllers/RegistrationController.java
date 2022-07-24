package com.example.kinocms.controllers;

import com.example.kinocms.entities.Role;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.RoleRepository;
import com.example.kinocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationButton(
            Model model,
            @Valid @ModelAttribute("user") User userForm,
            BindingResult bindingResult
    ) {
        userForm.setActive(true);
        userForm.todayRegistrationDate();

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userForm.getPassword().isEmpty()){
            model.addAttribute("msg", "Password shouldn't be empty");
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("msg", "Passwords are not equals");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("msg", "Login is already taken");
            return "registration";
        }
        return "redirect:/login";
    }
}