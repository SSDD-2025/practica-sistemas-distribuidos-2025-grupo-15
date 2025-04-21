package com.example.demo.dto;

import java.util.List;


public record UserDTO(String userName, String encodedPassword, int id, List<String> roles) {}