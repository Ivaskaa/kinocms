package com.example.kinocms.controllers.user.cinema;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.Schedule;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.cinemas.HallRepository;
import com.example.kinocms.service.ScheduleService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.cinemas.HallService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")  //для всього NewsController
public class HallControllerUser {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/hall/{id}/info")
    public String adminFilmsEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);

        Hall hall = hallRepository.findById(id).get();
        model.addAttribute("hall", hall);
        List<Schedule> schedules = scheduleService.findSchedulesByHall(hall);
        model.addAttribute("schedules", schedules);
        return "user/cinemas/halls/info";
    }
}
