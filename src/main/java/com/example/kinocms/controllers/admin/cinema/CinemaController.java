package com.example.kinocms.controllers.admin.cinema;

import com.example.kinocms.entities.*;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.repository.CityRepository;
import com.example.kinocms.repository.cinemas.HallRepository;
import com.example.kinocms.service.cinemas.CinemaService;
import com.example.kinocms.service.cinemas.HallService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CinemaController {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private HallService hallService;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private PageService pageService;

    @GetMapping( "/cinemas" )              // сторінка одразе перекидає на /admin/users/1
    public String getAllPages (){
        return  "redirect:/admin/cinemas/1";
    }

    @GetMapping("/cinemas/{pageNumber}")
    public String adminCinemas(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        //model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Page<Cinema> page = cinemaService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Cinema> cinemas = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("cinemas", cinemas);
        return "admin/cinemas/cinemas";
    }

    @GetMapping("/cinemas/add")
    public String adminCinemaAdd(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Iterable<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemas/add";
    }

    @PostMapping("/cinemas/add")
    public String adminCinemasAddButton(
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("cinema") Cinema cinemaForm,
            BindingResult bindingResult
    ) throws IOException {
        // model
        Iterable<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("user", user);
        model.addAttribute("cinema", cinemaForm);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/cinemas/add";
        }
        String cityValidation = cinemaService.cityValidation(cinemaForm.getCity());
        if(cityValidation != null){  // filmService validation
            model.addAttribute("cityValidation", cityValidation);
            return "admin/cinemas/add";
        }

        // save
        cinemaService.saveCinema(cinemaForm, file);
        return "redirect:/admin/cinemas";     // перенаправляє на /admin/cinemas
    }

    @GetMapping("/cinemas/{id}/edit")
    public String adminCinemaPages(
            @PathVariable(value = "id") Long id
    ){
        return "redirect:/admin/cinemas/" + id + "/edit/1";
    }

    @GetMapping("/cinemas/{id}/edit/{pageNumber}")
    public String adminCinemaEdit(
            @PathVariable("pageNumber") int currentPage,
            @PathVariable(value = "id") Long id,
            Model model
    ){
        if(!cinemaRepository.existsById(id)){
            return "admin/cinemas/cinemas";
        }
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // for cinema
        Iterable<City> cities = cityRepository.findAll();
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        Cinema fin = cinema.get();
        model.addAttribute("cities", cities);
        model.addAttribute("cinema", fin);

        // for list halls
        Page<Hall> page = hallService.findByCinemaId(fin, currentPage);
        int totalPages = page.getTotalPages();
        List<Hall> halls = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("halls", halls);
        return "admin/cinemas/edit";
    }

    @PostMapping("/cinemas/{id}/edit/{pageNumber}")
    public String adminCinemaEditButton(
            @PathVariable(value = "id") Long id,
            Model model,
            @PathVariable("pageNumber") int currentPage,
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute("cinema") Cinema cinemaForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // for cinema
        Iterable<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("cinema", cinemaForm);

        // for halls list
        Page<Hall> page = hallService.findByCinemaId(cinemaForm, currentPage);
        int totalPages = page.getTotalPages();
        List<Hall> halls = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("halls", halls);



        // validation
        if(bindingResult.hasErrors()){
            return "admin/cinemas/edit";
        }
        String cityValidation = cinemaService.cityValidation(cinemaForm.getCity());
        if(cityValidation != null){  // filmService validation
            model.addAttribute("cityValidation", cityValidation);
            return "admin/cinemas/add";
        }

        // save
        cinemaService.editCinema(cinemaForm, file, id);
        return "redirect:/admin/cinemas/";       // перенаправляє на /admin/cities
    }

    @GetMapping ("/cinemas/{id}/remove")
    public String adminCinemaDelete(
            @PathVariable(value = "id") Long id
    ){
        cinemaRepository.deleteById(id);
        return "redirect:/admin/cinemas";
    }
}
