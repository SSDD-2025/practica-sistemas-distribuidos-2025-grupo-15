package com.example.demo.dto;

import java.util.List;


public record UserDTO(String userName, String encondedPassword, int id, List<String> roles) {}