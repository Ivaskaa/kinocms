package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.*;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.repository.FilmRepository;
import com.example.kinocms.repository.ScheduleRepository;
import com.example.kinocms.service.ScheduleService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.cinemas.HallService;
import com.example.kinocms.service.pages.PageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Controller
@RequestMapping("/admin")
public class ScheduleController {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private HallService hallService;
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;

    @GetMapping("/schedules/getHalls")
    @ResponseBody   // json
    public String getHalls (Cinema cinema) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper(); // для роботи з JSON
        List<Hall> halls = hallService.findHallsByCinema(cinema);
        return mapper.writeValueAsString(halls);
    }

    @GetMapping( "/schedules" )              // сторінка одразе перекидає на /admin/news/1
    public String getAllPages (){
        return  "redirect:/admin/schedules/1";
    }

    @GetMapping("/schedules/{pageNumber}")
    public String adminSchedules(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        Page<Schedule> page = scheduleService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Schedule> schedules = page.getContent();

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("schedules", schedules);
        return "admin/schedules/schedules";
    }

    @GetMapping("/schedules/add")
    public String adminScheduleAdd(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        List<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        List<Cinema> cinemas = cinemaRepository.findAll();
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("schedule", new Schedule());
        return "admin/schedules/add";
    }

    @PostMapping("/schedules/add")
    public String adminScheduleAddButton(
            Model model,
            @ModelAttribute("schedule") Schedule scheduleForm,
            @RequestParam("priceString") String priceString,
            @RequestParam(value = "cinema", required = false) Cinema cinema // required true - форма обовязково має щось відправити
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Film> films = filmRepository.findAll();
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("films", films);
        List<Cinema> cinemas = cinemaRepository.findAll();
        model.addAttribute("cinemas", cinemas);
        if(cinema!=null) {
            model.addAttribute("cinema", cinema);
        }
        if(cinema != null) {
            List<Hall> halls = hallService.findHallsByCinema(cinema);
            model.addAttribute("halls", halls);
        }
        String formattedDate = scheduleService.formattedDate(scheduleForm);
        model.addAttribute("date", formattedDate);
        model.addAttribute("price", priceString);

        //validation
        String msg = scheduleService.validation(scheduleForm, priceString, cinema);
        if(msg != null){
            model.addAttribute("msg", msg);
            return "admin/schedules/add";
        }

        //save
        scheduleService.saveSchedule(scheduleForm, priceString);
        return "redirect:/admin/schedules";     // перенаправляє на /admin/news
    }

    @GetMapping("/schedules/{id}/edit")
    public String adminScheduleEdit(
            @PathVariable(value = "id") Long id,
            Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Schedule schedule = scheduleRepository.findById(id).get();
        model.addAttribute("schedule", schedule);
        List<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        List<Cinema> cinemas = cinemaRepository.findAll();
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("cinema", schedule.getHall().getCinema());
        List<Hall> halls = hallService.findHallsByCinema(schedule.getHall().getCinema());
        model.addAttribute("halls", halls);
        String formattedDate = scheduleService.formattedDate(schedule);
        model.addAttribute("date", formattedDate);
        model.addAttribute("price", schedule.getPrice().toString());
        System.out.println(schedule.isActive());
        return "admin/schedules/edit";
    }

    @PostMapping("/schedules/{id}/edit")
    public String adminScheduleEditButton(
            @PathVariable(value = "id") Long id,
            Model model,
            @ModelAttribute("schedule") Schedule scheduleForm,
            @RequestParam("priceString") String priceString,
            @RequestParam(value = "cinema", required = false) Cinema cinema
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        List<Film> films = filmRepository.findAll();
        model.addAttribute("films", films);
        List<Cinema> cinemas = cinemaRepository.findAll();
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("cinema", cinema);
        List<Hall> halls = hallService.findHallsByCinema(cinema);
        model.addAttribute("halls", halls);
        String formattedDate = scheduleService.formattedDate(scheduleForm);
        model.addAttribute("date", formattedDate);
        model.addAttribute("price", priceString);

        // validation
        String msg = scheduleService.validation(scheduleForm, priceString, cinema);
        if(msg != null){
            model.addAttribute("msg", msg);
            return "admin/schedules/edit";
        }

        // save
        scheduleService.editSchedule(scheduleForm, priceString, id);
        return "redirect:/admin/schedules";       // перенаправляє на /admin/news
    }

    @GetMapping ("/schedules/{id}/remove")
    public String adminSchedulesDelete(
            @PathVariable(value = "id") Long id
    ){
        scheduleRepository.deleteById(id);
        return "redirect:/admin/schedules";
    }
}
