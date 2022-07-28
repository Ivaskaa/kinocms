package com.example.kinocms.controllers;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.service.*;
import com.example.kinocms.service.cinemas.CinemaService;
import com.example.kinocms.service.cinemas.HallService;
import com.example.kinocms.service.pages.PageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private SpecialOffersService specialOffersService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/admin")
    public String admin(Model model) {
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        String countUsers = userService.findCountActiveUsers();
        model.addAttribute("countUsers", countUsers);
        String countFilms = filmService.findCountFilms();
        model.addAttribute("countFilms", countFilms);
        String countSchedules = scheduleService.findCountSchedules();
        model.addAttribute("countSchedules", countSchedules);
        String uniqueVisitors = userService.findCountUsers();
        model.addAttribute("uniqueVisitors", uniqueVisitors);
        return "admin/admin";
    }

    @GetMapping("/admin/donutChart")
    @ResponseBody   // json
    public String donutChart() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // для роботи з JSON
        List<Cinema> cinemas = cinemaService.findActiveAll();
        return mapper.writeValueAsString(cinemas);
    }

    @GetMapping("/admin/barChart")
    @ResponseBody   // json
    public String barChart() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // для роботи з JSON
        List<Integer> numbers = new ArrayList<>();
        numbers.add(newsService.findActiveNewsCount());
        numbers.add(specialOffersService.findActiveSpecialOffersCount());
        return mapper.writeValueAsString(numbers);
    }
}
