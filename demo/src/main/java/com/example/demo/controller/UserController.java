package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class UserController {

    @Autowired
    private UserService userService; 

    @GetMapping("/createAccount")
    public String createAccount(Model model){
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccountProcess(@ModelAttribute User user, Model model) {
        if (userService.userExists(user)) {
            model.addAttribute("errorMessage", "El nombre de usuario ya está registrado.");
            return "createAccount";  
        }
    
        userService.createUser(user);
        return "redirect:/";      
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String userName, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.getUser(userName);
        if(user == null){
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "login";
        }else if(!user.getPassword().equals(password)){
            model.addAttribute("errorMessage", "Contraseña incorrecta");
            return "login";
        }else{
            session.setAttribute("userId", user.getId());
            return "redirect:/";
        }
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId != null){
            model.addAttribute("loged", userService.getUser(userId).getUserName());
        }
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null){
            User user = userService.getUser(userId);
            if (user != null){
                model.addAttribute("userName",userService.getUser(userId).getUserName());
                return "profile";
            }
        }
        return "users";
    }
    
     @PostMapping("/profile")
    public String deleteUser(HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUser(userId);
            if (user != null){
                session.setAttribute("userId", null);
                userService.deleteUser(userId);
            }
        }else{
            return "errorNoexiste";
        }
        return "redirect:/";

    }
}

