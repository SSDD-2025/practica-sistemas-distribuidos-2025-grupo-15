package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        return "redirect:/users";      
    }



    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }
    
    

}
