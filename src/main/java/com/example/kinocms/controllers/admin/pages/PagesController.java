package com.example.kinocms.controllers.admin.pages;

import com.example.kinocms.entities.News;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.pages.PageRepository;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class PagesController {
    @Autowired
    private PageService pageService;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private UserService userService;

    @GetMapping( "/pages" )              // сторінка одразе перекидає на /admin/news/1
    public String getAllPages (){
        return  "redirect:/admin/pages/1";
    }

    @GetMapping("/pages/{pageNumber}")
    public String adminPages(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        Page<com.example.kinocms.entities.pages.Page> page = pageService.findCommonPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        List<com.example.kinocms.entities.pages.Page> mainPages = pageService.findMainPage();
        model.addAttribute("mainPages", mainPages);
        List<com.example.kinocms.entities.pages.Page> pages = page.getContent();
        model.addAttribute("pages", pages);
        return "admin/pages/pages";
    }

    @GetMapping("/pages/add")
    public String adminPageAdd(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        model.addAttribute("page", new com.example.kinocms.entities.pages.Page());
        return "admin/pages/add";
    }

    @PostMapping("/pages/add")
    public String adminPageAddButton(
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("page") com.example.kinocms.entities.pages.Page pageForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/pages/add";
        }

        //save
        pageService.savePage(pageForm, file);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/{id}/edit")
    public String adminPageEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        com.example.kinocms.entities.pages.Page page = pageRepository.findById(id).get();
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }

    @PostMapping("/pages/{id}/edit")
    public String adminPageEditButton(
            @PathVariable(value = "id") Long id,
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("page") com.example.kinocms.entities.pages.Page pageForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/pages/edit";
        }

        //save
        pageService.editPage(pageForm, file, id);
        return "redirect:/admin/pages";
    }

    @GetMapping ("/pages/{id}/remove")
    public String adminPageDelete(
            @PathVariable(value = "id") Long id
    ){
        pageRepository.deleteById(id);
        return "redirect:/admin/pages";
    }
}
