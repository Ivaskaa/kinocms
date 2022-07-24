package com.example.kinocms.controllers.user.cinema;

import com.example.kinocms.entities.*;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.FilmRepository;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.service.FilmService;
import com.example.kinocms.service.ScheduleService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.cinemas.CinemaService;
import com.example.kinocms.service.cinemas.HallService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/user")  //для всього NewsController
public class CinemaControllerUser {

    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private HallService hallService;

    @GetMapping( "/cinemas" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/user/cinemas/1";
    }

    @GetMapping("/cinemas/{pageNumber}")
    public String adminFilms(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        org.springframework.data.domain.Page<Cinema> page = cinemaService.findActivePage(currentPage);
        int totalPages = page.getTotalPages();
        List<Cinema> cinemas = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("cinemas", cinemas);
        return "user/cinemas/cinemas";
    }

    @GetMapping("/cinemas/{id}/info")
    public String adminFilmsEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);

        Cinema cinema = cinemaRepository.findById(id).get();
        model.addAttribute("cinema", cinema);
        List<Hall> halls = hallService.findHallsByCinema(cinema);
        model.addAttribute("halls", halls);
        return "user/cinemas/info";
    }

}
