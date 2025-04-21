package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControler {
    @GetMapping("/error")
    public String error() {
        return "errorNoSession";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "errorNoSession";
    }

    @GetMapping("/errorNoSession")
    public String errorNoSession() {
        return "errorNoSession";
    }
}
