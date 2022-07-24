package com.example.kinocms.controllers.user;

import com.example.kinocms.entities.Cinema;
import com.example.kinocms.entities.Hall;
import com.example.kinocms.entities.News;
import com.example.kinocms.entities.User;
import com.example.kinocms.entities.pages.Page;
import com.example.kinocms.repository.NewsRepository;
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
public class NewsControllerUser {
    @Autowired
    private UserService userService;
    @Autowired
    private PageService pageService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping( "/news" )              // сторінка одразе перекидає на /admin/films/1
    public String getAllPages (){
        return  "redirect:/user/news/1";
    }

    @GetMapping("/news/{pageNumber}")
    public String adminNews(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        // model
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);

        org.springframework.data.domain.Page<News> page = newsService.findActivePage(currentPage);
        int totalPages = page.getTotalPages();
        List<News> news = page.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("news", news);
        return "user/news/news";
    }

    @GetMapping("/news/{id}/info")
    public String adminNewsInfo(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        // model
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        User user = userService.loadUserByAuthentication(SecurityContextHolder.getContext().getAuthentication()); // знаходжу user в базі даних
        model.addAttribute("user", user);

        News news = newsRepository.findById(id).get();
        model.addAttribute("news", news);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted;
        if(news.getDate() == null) {
            formatted = "";
        } else {
            formatted = format.format(news.getDate());
        }

        model.addAttribute("date", formatted);
        return "user/news/info";
    }
}
