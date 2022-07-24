package com.example.kinocms.controllers.admin.pages;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.City;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Contact;
import com.example.kinocms.repository.cinemas.CinemaRepository;
import com.example.kinocms.repository.CityRepository;
import com.example.kinocms.repository.pages.ContactRepository;
import com.example.kinocms.service.cinemas.CinemaService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.ContactService;
import com.example.kinocms.service.pages.PageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ContactsController {
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PageService pageService;

    // for ajax
    @GetMapping("/contacts/getCinemas")
    @ResponseBody   // json
    public String getCinemas(City city) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // для роботи з JSON
        List<Cinema> cinemas = cinemaService.findCinemasByCity(city);
        return mapper.writeValueAsString(cinemas);
    }

    @GetMapping( "/contacts" )              // сторінка одразе перекидає на /admin/news/1
    public String getAllPages (){
        return  "redirect:/admin/contacts/1";
    }

    @GetMapping("/contacts/{pageNumber}")
    public String adminContact(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        Page<Contact> page = contactService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Contact> contacts = page.getContent();

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("contacts", contacts);
        return "admin/pages/contacts/contacts";
    }

    @GetMapping("/contacts/add")
    public String adminContactAdd(Model model){
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("contact", new Contact());
        return "admin/pages/contacts/add";
    }

    @PostMapping("/contacts/add")
    public String adminContactAddButton(
            Model model,
            @Valid @ModelAttribute("contact") Contact contactForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        List<Cinema> cinemas = cinemaRepository.findCinemasByCity(contactForm.getCity());
        model.addAttribute("cinemas", cinemas);

        // validation
        String msg = contactService.validation(contactForm);
        if(msg != null){
            model.addAttribute("msg", msg);
            return "admin/pages/contacts/add";
        }
        if(bindingResult.hasErrors()){
            return "admin/pages/contacts/add";
        }

        // save
        contactService.saveContact(contactForm);
        return "redirect:/admin/contacts";
    }

    @GetMapping("/contacts/{id}/edit")
    public String adminContactEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Contact contact = contactRepository.findById(id).get();
        model.addAttribute("contact", contact);
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        if(contact.getCity() != null) {
            List<Cinema> cinemas = cinemaRepository.findCinemasByCity(contact.getCity());
            model.addAttribute("cinemas", cinemas);
        }
        return "admin/pages/contacts/edit";
    }

    @PostMapping("/contacts/{id}/edit")
    public String adminNewsEditButton(
            @PathVariable(value = "id") Long id,
            Model model,
            @Valid @ModelAttribute("contact") Contact contactForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        List<Cinema> cinemas = cinemaRepository.findCinemasByCity(contactForm.getCity());
        model.addAttribute("cinemas", cinemas);

        // validation
        String msg = contactService.validation(contactForm);
        if(msg != null){
            model.addAttribute("msg", msg);
            return "admin/pages/contacts/edit";
        }
        if(bindingResult.hasErrors()){
            return "admin/pages/contacts/edit";
        }

        // save
        contactService.editContact(contactForm, id);
        return "redirect:/admin/contacts";
    }

    @GetMapping ("/contacts/{id}/remove")
    public String adminContactDelete(
            @PathVariable(value = "id") Long id
    ){
        contactRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}
