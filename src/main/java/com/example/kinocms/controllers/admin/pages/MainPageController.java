package com.example.kinocms.controllers.admin.pages;

import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.MainPage;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.pages.MainPageRepository;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.MainPageService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")  //для всього NewsController
public class MainPageController {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private MainPageService mainPageService;
    @Autowired
    private MainPageRepository mainPageRepository;


    @GetMapping("/mainPage")
    public String adminMainPage(
            Model model
    ){
        MainPage mainPage = mainPageRepository.findById(1L).get();

        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("mainPage", mainPage);
        return "admin/pages/mainPage/edit";
    }


    @PostMapping("/mainPage")
    public String adminMainPageEditButton(
            Model model,
            @Valid @ModelAttribute("mainPage") MainPage mainPageForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);


        // validation
        if(bindingResult.hasErrors()){ // bindingResult validation
            return "admin/pages/mainPage/edit";
        }
        String firstNumberValidation = mainPageService.numberValidation(mainPageForm.getFirstNumber());
        if(firstNumberValidation != null){  // filmService validation
            model.addAttribute("firstNumberValidation", firstNumberValidation);
            return "admin/pages/mainPage/edit";
        }
        String secondNumberValidation = mainPageService.numberValidation(mainPageForm.getSecondNumber());
        if(secondNumberValidation != null){  // filmService validation
            model.addAttribute("secondNumberValidation", secondNumberValidation);
            return "admin/pages/mainPage/edit";
        }

        // save
        mainPageService.save(mainPageForm);
        return "redirect:/admin/pages";
    }
}
