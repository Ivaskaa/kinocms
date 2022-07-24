package com.example.kinocms.controllers.user.pages;

import com.example.kinocms.entities.News;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Contact;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.NewsRepository;
import com.example.kinocms.service.NewsService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.ContactService;
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
@RequestMapping("/user")
public class ContactControllerUser {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private ContactService contactService;

    @GetMapping( "/contacts" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/user/contacts/1";
    }

    @GetMapping("/contacts/{pageNumber}")
    public String adminContacts(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        org.springframework.data.domain.Page<Contact> page = contactService.findActivePage(currentPage);
        int totalPages = page.getTotalPages();
        List<Contact> contacts = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("contacts", contacts);
        return "user/pages/contacts/contacts";
    }
}
