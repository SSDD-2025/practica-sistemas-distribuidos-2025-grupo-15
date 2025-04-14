package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
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

    @Autowired
    UserMapper userMapper;

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
        userService.createUser(userMapper.toDTO(user));
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username, @RequestParam String password,
            Model model) {
        UserDTO userDTO = userService.getUser(username);
        if (userDTO == null) {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "login";
        } else if (!userDTO.encondedPassword().equals(password)) {
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
            model.addAttribute("loged", userService.getUser(userId).userName());
        }
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();

        UserDTO userDTO = userService.getUser(name);
        if (userDTO != null) {
            model.addAttribute("userName", userDTO.userName());
            return "profile";
        }

        return "errorNoSesion";
    }

    @PostMapping("/profile")
    public String deleteUser(HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        if (userDTO != null) {
            userService.deleteUser(userDTO.id());
        }
        return "redirect:/";
    }

    @GetMapping("/editProfile")
    public String editProfile(HttpServletRequest request, Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(currentUsername);

        if (userDTO != null) {
            return "editProfile";
        }
        return "home";
    }

    @PostMapping("/saveEditProfile")
    public String saveEditedProfile(HttpServletRequest request, @RequestParam String userName, 
                                    @RequestParam(required = false) String password, 
                                    Model model) {
        String currentUsername = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(currentUsername);
        User user = userMapper.toDomain(userDTO);

        if (userDTO != null) {
            if (!userDTO.userName().equals(userName)) {
                if (userService.getUser(userName) != null) {
                    model.addAttribute("error", "El nombre de usuario ya está en uso.");
                    model.addAttribute("userName", userDTO.userName()); 
                    return "editProfile";
                }
                user.setUserName(userName); 
            }

           
            if (password != null && !password.isBlank()) {
                user.setEncodedPassword(passwordEncoder.encode(password));
            }

            userDTO = userMapper.toDTO(user);
            userService.updateUser(userDTO.id(), userDTO);

        }

        return "redirect:/profile"; 
    }
}
