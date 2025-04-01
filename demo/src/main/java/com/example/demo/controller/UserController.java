package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/createAccount")
    public String createAccount(Model model) {
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccountProcess(@RequestParam String userName,  @RequestParam String password, Model model) {
        if (userService.getUser(userName) != null) {
            model.addAttribute("errorMessage", "El nombre de usuario ya está registrado.");
            return "createAccount";
        }

        User user = new User(userName, passwordEncoder.encode(password), new ArrayList<>(Arrays.asList("USER")));
        userService.createUser(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username, @RequestParam String password,
            Model model) {
        User user = userService.getUser(username);
        if (user == null) {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "login";
        } else if (!user.getEncodedPassword().equals(password)) {
            model.addAttribute("errorMessage", "Contraseña incorrecta");
            return "login";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("loged", userService.getUser(userId).getUserName());
        }
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();

        User user = userService.getUser(name);
        if (user != null) {
            model.addAttribute("userName", user.getUserName());
            return "profile";
        }

        return "errorNoSesion";
    }

    @PostMapping("/profile")
    public String deleteUser(HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        if (user != null) {
            userService.deleteUser(user.getId());
        }
        return "redirect:/";
    }
}
