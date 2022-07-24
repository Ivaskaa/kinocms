package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.SpecialOffer;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.SpecialOffersRepository;
import com.example.kinocms.service.SpecialOffersService;
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

@Controller
@RequestMapping("/admin")
public class SpecialOffersController {
    @Autowired
    private SpecialOffersRepository specialOffersRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SpecialOffersService specialOffersService;
    @Autowired
    private PageService pageService;

    @GetMapping ( "/specialOffers" )              // сторінка одразе перекидає на /admin/news/1
    public String getAllPages (){
        return  "redirect:/admin/specialOffers/1";
    }

    @GetMapping("/specialOffers/{pageNumber}")
    public String adminNews(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        Page<SpecialOffer> page = specialOffersService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<SpecialOffer> specialOffers = page.getContent();

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("specialOffers", specialOffers);
        return "admin/specialOffers/specialOffers";
    }

    @GetMapping("/specialOffers/add")
    public String adminSpecialOffersAddSettings(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("specialOffer", new SpecialOffer());
        return "admin/specialOffers/add";
    }

    @PostMapping("/specialOffers/add")
    public String adminSpecialOffersAdd(
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("specialOffer") SpecialOffer specialOfferForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/specialOffers/add";
        }

        //save
        specialOffersService.saveSpecialOffer(specialOfferForm, file);
        return "redirect:/admin/specialOffers";     // перенаправляє на /admin/specialOffers
    }

    @GetMapping("/specialOffers/{id}/edit")
    public String adminSpecialOffersEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        if(!specialOffersRepository.existsById(id)){
            return "admin/specialOffers";
        }
        //model
        SpecialOffer specialOffer = specialOffersRepository.findById(id).get();
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("specialOffer", specialOffer);
        return "admin/specialOffers/edit";
    }

    @PostMapping("/specialOffers/{id}/edit")
    public String adminNewsEditUpdate(
            @RequestParam("file") MultipartFile file,
            @PathVariable(value = "id") Long id,
            Model model,
            @Valid @ModelAttribute("specialOffer") SpecialOffer specialOfferForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        //model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/specialOffers/edit";
        }

        //save
        specialOffersService.editSpecialOffer(specialOfferForm, file, id);
        return "redirect:/admin/specialOffers";
    }

    @GetMapping ("/specialOffers/{id}/remove")
    public String adminNewsDelete(
            @PathVariable(value = "id") Long id
    ){
        specialOffersRepository.deleteById(id);
        return "redirect:/admin/specialOffers";
    }
}
