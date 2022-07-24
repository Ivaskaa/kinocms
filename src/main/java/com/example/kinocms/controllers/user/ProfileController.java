package com.example.kinocms.controllers.user;

import com.example.kinocms.entities.City;
import com.example.kinocms.entities.User;
import com.example.kinocms.repository.CityRepository;
import com.example.kinocms.repository.UserRepository;
import com.example.kinocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;


@Controller
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping( "/profile" )              // сторінка одразе перекидає на /admin/users/1
    public String getUserProfile (
            Model model
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        return "user/profile/profile";
    }

    @GetMapping( "/profile/edit" )              // сторінка одразе перекидає на /admin/users/1
    public String getUserProfileEdit (
            Model model
    ) {
        Iterable<City> cities = cityRepository.findAll();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted;
        if(user.getBirthday() != null){
            formatted = format.format(user.getBirthday());
        } else {
            formatted = "";
        }

        model.addAttribute("cities", cities);
        model.addAttribute("date", formatted);
        model.addAttribute("user", user);
        model.addAttribute("userForm", user);
        return "user/profile/edit";
    }

    @PostMapping("/profile/edit")
    public String userProfileEditButton(
            Model model,
            @Valid @ModelAttribute("userForm") User userForm,
            BindingResult bindingResult             // має завжди іти після обекту
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted;
        if(user.getBirthday() != null){
            formatted = format.format(user.getBirthday());
        } else {
            formatted = "";
        }

        Iterable<City> cities = cityRepository.findAll();

        model.addAttribute("date", formatted);
        model.addAttribute("cities", cities);
        model.addAttribute("user", user);

        if(bindingResult.hasErrors()){
            return "user/profile/edit";
        }

        User finalUser = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних
        if(!auth.getName().equals(userForm.getUsername())) {
            try{
                userService.loadUserByUsername(userForm.getUsername());
                model.addAttribute("usernameValidation", "User name already exists");
                return "user/profile/edit";
            } catch (Exception e){
                finalUser.setUsername(userForm.getUsername());
            }
        }
        finalUser.setEmail(userForm.getEmail());
        finalUser.setPhone(userForm.getPhone());
        finalUser.setBirthday(userForm.getBirthday());
        if (userForm.getCity() != null){
            finalUser.setCity(userForm.getCity());
        }
        userRepository.save(finalUser);
        return "redirect:/user/profile";
    }

    @GetMapping ("/profile/password")
    public String userProfilePassword(
            Model model
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних

        model.addAttribute("user", user);
        return "user/profile/password";
    }

    @PostMapping("/profile/password")
    public String userProfilePasswordButton(
            Model model,
            @RequestParam("password") String password,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("passwordConfirm") String passwordConfirm
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних
        model.addAttribute("user", user);
        if (password.isEmpty()){
            model.addAttribute("password", "Password shouldn't be empty");
            return "user/profile/password";
        }
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            model.addAttribute("password", "Wrong password");
            return "user/profile/password";
        } // спочатку записується пароль який прийшов з форми, потім хешований з бази даних
        if (newPassword.isEmpty()){
            model.addAttribute("newPassword", "New password shouldn't be empty");
            return "user/profile/password";
        }
        if (passwordConfirm.isEmpty()){
            model.addAttribute("passwordConfirm", "Repeat password shouldn't be empty");
            return "user/profile/password";
        }
        if(!newPassword.equals(passwordConfirm)){
            model.addAttribute("passwordConfirm", "Passwords are not equals");
            return "user/profile/password";
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return "redirect:/user/profile";
    }

    @GetMapping ("/profile/inactive")
    public String userProfileInactive(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // з сесії получаю user
        User user = userService.loadUserByUsername(auth.getName()); // знаходжу user в базі даних
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/logout";
    }
}
