package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.Film;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.FilmRepository;
import com.example.kinocms.service.FilmService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/admin")  //для всього NewsController
public class FilmController {
    @Autowired
    private UserService userService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmService filmService;
    @Autowired
    private PageService pageService;

    @GetMapping( "/films" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/admin/films/1";
    }

    @GetMapping("/films/{pageNumber}")
    public String adminFilms(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Page<Film> page = filmService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Film> films = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("films", films);
        return "admin/films/films";
    }

    @GetMapping("/films/add")
    public String adminFilmsAdd(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("film", new Film());
        return "admin/films/add";
    }

    @PostMapping("/films/add")
    public String adminFilmsAddButton(
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("film") Film filmForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){ // bindingResult validation
            return "admin/films/add";
        }
        if(filmService.timeValidation(filmForm.getTime()) != null){  // filmService validation
            String timeValidation = filmService.timeValidation(filmForm.getTime());
            model.addAttribute("timeValidation", timeValidation);
            return "admin/films/add";
        }
        if(filmService.dateValidation(filmForm.getMovieRelease()) != null){  // filmService validation
            String dateValidation = filmService.dateValidation(filmForm.getMovieRelease());
            model.addAttribute("dateValidation", dateValidation);
            return "admin/films/add";
        }

        // save
        filmService.saveFilm(filmForm, file);
        return "redirect:/admin/films";     // перенаправляє на /admin/films
    }

    @GetMapping("/films/{id}/edit")
    public String adminFilmsEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        if(!filmRepository.existsById(id)){
            return "admin/films";
        }

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
        model.addAttribute("film", film);
        return "admin/films/edit";
    }

    @PostMapping("/films/{id}/edit")
    public String adminFilmsEditButton(
            @PathVariable(value = "id") Long id,
            Model model,
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute("film") Film filmForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted;
        if(filmForm.getMovieRelease() == null) {
            formatted = "";
        } else {
            formatted = format.format(filmForm.getMovieRelease());
        }
        model.addAttribute("date", formatted);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/films/edit";
        }
        String timeValidation = filmService.timeValidation(filmForm.getTime());
        if(timeValidation != null){
            model.addAttribute("timeValidation", timeValidation);
            return "admin/films/edit";
        }
        String dateValidation = filmService.dateValidation(filmForm.getMovieRelease());
        if(dateValidation != null){
            model.addAttribute("dateValidation", dateValidation);
            return "admin/films/edit";
        }

        //save
        filmService.editFilm(filmForm, file, id);
        return "redirect:/admin/films";       // перенаправляє на /admin/films
    }

    @GetMapping ("/films/{id}/remove")
    public String adminFilmsDelete(
            @PathVariable(value = "id") Long id
    ){
        filmRepository.deleteById(id);
        return "redirect:/admin/films";
    }
}