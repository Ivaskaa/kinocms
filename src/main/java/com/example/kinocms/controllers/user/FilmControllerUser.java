package com.example.kinocms.controllers.user;

import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.Schedule;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.FilmRepository;
import com.example.kinocms.service.FilmService;
import com.example.kinocms.service.ScheduleService;
import com.example.kinocms.service.UserService;
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
public class FilmControllerUser {
    @Autowired
    private UserService userService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmService filmService;
    @Autowired
    private PageService pageService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping( "/films" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/user/films/1";
    }

    @GetMapping("/films/{pageNumber}")
    public String adminFilms(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        org.springframework.data.domain.Page<Film> page = filmService.findActivePage(currentPage);
        int totalPages = page.getTotalPages();
        List<Film> films = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("films", films);
        return "user/films/films";
    }

    @GetMapping("/films/{id}/info")
    public String adminFilmsEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        Film film = filmRepository.findById(id).get();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted;
        if(film.getMovieRelease() == null) {
            formatted = "";
        } else {
            formatted = format.format(film.getMovieRelease());
        }


        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        model.addAttribute("date", formatted);
        List<Schedule> schedules = scheduleService.findSchedulesByFilm(film);
        model.addAttribute("schedules", schedules);
        model.addAttribute("film", film);
        return "user/films/info";
    }
}
