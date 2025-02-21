package com.example.demo.DataBase;

import org.springframework.stereotype.Component;

import com.example.demo.User.User;
import com.example.demo.User.UserService;

import jakarta.annotation.PostConstruct;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;


@Component
public class DataBaseInitializer {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() throws IOException {
        User user1 = new User("Nombre de usuario", "Contraseña");
        user1.setId(1);
        userService.createUser(user1);

    }
}
