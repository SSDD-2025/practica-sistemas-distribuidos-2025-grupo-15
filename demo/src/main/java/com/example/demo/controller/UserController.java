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

    @GetMapping("/editProfile")
    public String editProfile(HttpServletRequest request, Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        User user = userService.getUser(currentUsername);

        if (user != null) {
            return "editProfile";
        }
        return "home";
    }

    @PostMapping("/saveEditProfile")
    public String saveEditedProfile(HttpServletRequest request, @RequestParam String userName, 
                                    @RequestParam(required = false) String password, 
                                    Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        User user = userService.getUser(currentUsername);

        if (user != null) {
            // Solo verificar si el nombre de usuario ha cambiado
            if (!user.getUserName().equals(userName)) {
                if (userService.getUser(userName) != null) {
                    model.addAttribute("error", "El nombre de usuario ya está en uso.");
                    model.addAttribute("userName", user.getUserName());  // Mantener el nombre actual en el formulario
                    return "editProfile";
                }
                user.setUserName(userName); // Actualizar solo si cambia
            }

            // Actualizar la contraseña si se ingresó una nueva
            if (password != null && !password.isBlank()) {
                user.setEncodedPassword(passwordEncoder.encode(password));
            }

            // Guardar cambios en la base de datos
            userService.updateUser(user);
        }

        return "redirect:/profile"; 
    }
}
