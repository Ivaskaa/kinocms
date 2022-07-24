package com.example.kinocms.controllers.admin;

import com.example.kinocms.entities.City;
import com.example.kinocms.entities.Role;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.CityRepository;
import com.example.kinocms.repository.UserRepository;
import com.example.kinocms.service.UserService;
import com.example.kinocms.service.pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PageService pageService;

    @GetMapping ( "/users" )              // сторінка одразе перекидає на /admin/users/1
    public String getAllPages (){
        return  "redirect:/admin/users/1";
    }

    @GetMapping("/users/{pageNumber}")
    public String adminUsers(
            Model model,
            @PathVariable("pageNumber") int currentPage
    ){
        Page<User> page = userService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<User> users = page.getContent();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("users", users);
        return "admin/users/users";
    }

    @GetMapping("/users/{id}/edit")
    public String adminUsersEdit(
            @PathVariable(value = "id") Long id,   // динамический параметр из url
            Model model
    ){
        if(!userRepository.existsById(id)){
            return "admin/users/users";
        }
        Optional<User> userFind = userRepository.findById(id);
        User user = userFind.get();
        boolean isAdmin = false;
        if(user.getRoles().contains(new Role((long)2, "ROLE_ADMIN"))){
            isAdmin = true;
        }

        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        Iterable<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("user", user);
        model.addAttribute("userForm", user);
        model.addAttribute("isAdmin", isAdmin);
        return "admin/users/edit";
    }

    @PostMapping("/users/{id}/edit")
    public String adminNewsEditButton(
            @PathVariable(value = "id") Long id,
            @RequestParam(required=false) boolean admin,
            Model model,
            @Valid @ModelAttribute("userForm") User userForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ){
        Optional<User> userFind = userRepository.findById(id);
        User user = userFind.get();

        boolean isAdmin = false;
        if(user.getRoles().contains(new Role((long)2, "ROLE_ADMIN"))){
            isAdmin = true;
        }

        Iterable<City> cities = cityRepository.findAll();
        List<com.example.kinocms.entities.pages.Page> pages = pageService.findMainPage();
        model.addAttribute("mainPages", pages);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("cities", cities);
        model.addAttribute("user", user);
        if(bindingResult.hasErrors()){
            return "admin/users/edit";
        }
        User finalUser = userRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        finalUser.setUsername(userForm.getUsername());
        finalUser.setEmail(userForm.getEmail());
        finalUser.setPhone(userForm.getPhone());
        finalUser.setActive(userForm.isActive());
        finalUser.setCity(userForm.getCity());
        Role role = new Role ((long)2, "ROLE_ADMIN");
        if(admin) {
            if(!finalUser.getRoles().contains(role)) {
                finalUser.getRoles().add(role);
            }
        } else {
            if(finalUser.getRoles().contains(role)) {
                finalUser.getRoles().remove(role);
            }
        }
        userRepository.save(finalUser);
        return "redirect:/admin/users";       // перенаправляє на /admin/users
    }

    @GetMapping ("/users/{id}/remove")
    public String adminUserDelete(
            @PathVariable(value = "id") Long id
    ){
        User user = userRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        Role roleUser = new Role ((long)1, "ROLE_USER");
        Role roleAdmin = new Role ((long)2, "ROLE_ADMIN");
        if(user.getRoles().contains(roleAdmin)) {
            user.getRoles().remove(roleAdmin);
        }
        if(user.getRoles().contains(roleUser)) {
            user.getRoles().remove(roleUser);
        }
        userRepository.delete(user);
        return "redirect:/admin/users";       // перенаправляє на /admin/users
    }

}