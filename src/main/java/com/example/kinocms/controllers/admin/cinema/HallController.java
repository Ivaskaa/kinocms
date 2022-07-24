package com.example.kinocms.controllers.admin.cinema;

import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.repository.cinemas.HallRepository;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.cinemas.HallService;
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
@RequestMapping("/admin/cinemas/{id}")
public class HallController {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private HallService hallService;
    @Autowired
    private PageService pageService;
    @GetMapping("/add")

    public String adminHallAdd(
            Model model
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("hall", new Hall());
        return "admin/cinemas/halls/add";
    }

    @PostMapping("/add")
    public String adminHallAddButton(
            @RequestParam("file") MultipartFile file,
            @PathVariable(value = "id") Long id,
            Model model,
            @Valid @ModelAttribute("hall") Hall hallForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/cinemas/halls/add";
        }
        String numberValidation = hallService.numberValidation(hallForm.getNumber());
        if(numberValidation != null){  // filmService validation
            model.addAttribute("numberValidation", numberValidation);
            return "admin/cinemas/halls/edit";
        }

        // save
        hallService.saveHall(hallForm, file, id);
        return "redirect:/admin/cinemas/" + id + "/edit";
    }

    @GetMapping("/{hallId}/edit")
    public String adminHallEdit(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "hallId") Long hallId,
            Model model
    ){
        if(!cinemaRepository.existsById(id)){
            return "redirect:admin/cinemas/" + id + "/edit";
        }
        if(!hallRepository.existsById(hallId)){
            return "redirect:admin/cinemas/" + id + "/edit";
        }

        //model

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Hall hall = hallRepository.findById(hallId).get();
        model.addAttribute("hall", hall);
        return "admin/cinemas/halls/edit";
    }

    @PostMapping("/{hallId}/edit")
    public String adminHallEditButton(
            @RequestParam("file") MultipartFile file,
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "hallId") Long hallId,
            Model model,
            @Valid @ModelAttribute("hall") Hall hallForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        //model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("hall", hallForm);

        //validation
        if(bindingResult.hasErrors()){
            return "admin/cinemas/halls/edit";
        }
        String numberValidation = hallService.numberValidation(hallForm.getNumber());
        if(numberValidation != null){  // filmService validation
            model.addAttribute("numberValidation", numberValidation);
            return "admin/cinemas/halls/edit";
        }

        //save
        hallService.editHall(hallForm, file, hallId);
        return "redirect:/admin/cinemas/" + id + "/edit";
    }

    @GetMapping ("/{hallId}/remove")
//    @Transactional // при масовому видаленні
    public String adminCinemaDelete(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "hallId") Long hallId
    ){
        hallRepository.myDeleteById(hallId); // FIXME: 18.07.2022
        return "redirect:/admin/cinemas/" + id + "/edit";
    }
}
