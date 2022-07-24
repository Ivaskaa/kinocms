package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.City;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.CityRepository;
import com.example.kinocms.service.CityService;
//import com.example.kinocms.service.MailSender;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CityController {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;
    @Autowired
    private PageService pageService;
    @Autowired
    private JavaMailSender emailSender;
//    @Autowired
//
//    private MailSender mailSender;
    @GetMapping( "/cities" )              // сторінка одразе перекидає на /admin/users/1
    public String getAllPages (){
        return  "redirect:/admin/cities/1";
    }

    @GetMapping("/cities/{pageNumber}")
    public String adminCities(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        Page<City> page = cityService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<City> cities = page.getContent();

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("cities", cities);
        return "admin/cities/cities";
    }

    @GetMapping("/cities/add")
    public String adminCitiesAdd(Model model){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sazb.serg@gmail.com");
        mailMessage.setTo("cmskino@gmail.com");
        mailMessage.setSubject("subject");
        mailMessage.setText("message");
        emailSender.send(mailMessage);

        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("city", new City());
        return "admin/cities/add";
    }

    @PostMapping("/cities/add")
    public String adminCitiesAddButton(
            Model model,
            @Valid @ModelAttribute("city") City cityForm,
            BindingResult bindingResult
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/cities/add";
        }

        //save
        cityService.saveCity(cityForm);
        return "redirect:/admin/cities";     // перенаправляє на /admin/news
    }

    @GetMapping("/cities/{id}/edit")
    public String adminCitiesEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        City city = cityRepository.findById(id).get();
        model.addAttribute("city", city);
        return "admin/cities/edit";
    }

    @PostMapping("/cities/{id}/edit")
    public String adminCitiesEditButton(
            @PathVariable(value = "id") Long id,
            Model model,
            @Valid @ModelAttribute("city") City cityForm,
            BindingResult bindingResult
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/cities/edit";
        }

        //save
        cityService.editCity(cityForm, id);
        return "redirect:/admin/cities";       // перенаправляє на /admin/cities
    }

    @GetMapping ("/cities/{id}/remove")
    public String adminCitiesDelete(
            @PathVariable(value = "id") Long id
    ){
        cityRepository.deleteById(id);
        return "redirect:/admin/cities";       // перенаправляє на /admin/cities
    }
}
