package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.News;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.NewsRepository;
import com.example.kinocms.service.NewsService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")  //для всього NewsController
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private PageService pageService;

    @GetMapping ( "/news" )              // сторінка одразе перекидає на /admin/news/1
    public String getAllPages (){
        return  "redirect:/admin/news/1";
    }

    @GetMapping("/news/{pageNumber}")
    public String adminNews(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        Page<News> page = newsService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        List<News> news = page.getContent();

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("news", news);
        return "admin/news/news";
    }

    @GetMapping("/news/add")
    public String adminNewsAdd(Model model){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("news", new News());
        return "admin/news/add";
    }

    @PostMapping("/news/add")
    public String adminNewsAddButton(
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("news") News newsForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        //validation
        if(bindingResult.hasErrors()){
            return "admin/news/add";
        }

        //save
        newsService.saveNews(newsForm, file);
        return "redirect:/admin/news";     // перенаправляє на /admin/news
    }

    @GetMapping("/news/{id}/edit")
    public String adminNewsEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        if(!newsRepository.existsById(id)){
            return "admin/news";
        }

        // model

        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        News news = newsRepository.findById(id).get();
        model.addAttribute("news", news);
        return "admin/news/edit";
    }

    @PostMapping("/news/{id}/edit")
    public String adminNewsEditButton(
            @PathVariable(value = "id") Long id,
            @RequestParam("file") MultipartFile file,
            Model model,
            @Valid @ModelAttribute("news") News newsForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ) throws IOException {
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        // validation
        if(bindingResult.hasErrors()){
            return "admin/news/edit";
        }

        // save
        newsService.editNews(newsForm, file, id);
        return "redirect:/admin/news";       // перенаправляє на /admin/news
    }

    @GetMapping ("/news/{id}/remove")
    public String adminNewsDelete(
            @PathVariable(value = "id") Long id
    ){
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
}
