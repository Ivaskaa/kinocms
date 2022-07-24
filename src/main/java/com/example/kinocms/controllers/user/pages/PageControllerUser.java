package com.example.kinocms.controllers.user.pages;

import com.example.kinocms.entities.News;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.NewsRepository;
import com.example.kinocms.repository.pages.PageRepository;
import com.example.kinocms.service.NewsService;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/user")
public class PageControllerUser {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private PageRepository pageRepository;

    @GetMapping( "/pages" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/user/pages/1";
    }

    @GetMapping("/pages/{pageNumber}")
    public String adminNews(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        org.springframework.data.domain.Page<Page> page = pageService.findActiveCommonPage(currentPage);
        int totalPages = page.getTotalPages();
        List<Page> modelPages = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pages", modelPages);
        return "user/pages/pages";
    }

    @GetMapping("/pages/{id}/info")
    public String adminNewsInfo(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);

        Page page = pageRepository.findById(id).get();
        model.addAttribute("page", page);
        return "user/pages/info";
    }
}
