package com.example.kinocms.controllers;

import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class MainController {

    @Autowired
    private PageService pageService;

    @GetMapping("/")
    public String main(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        model.addAttribute("auth", auth);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        return "user/user";
    }
}
