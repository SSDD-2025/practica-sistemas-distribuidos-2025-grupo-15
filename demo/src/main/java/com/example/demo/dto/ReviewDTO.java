package com.example.demo.dto;

public record ReviewDTO(
        int id,
        UserDTO reviewUser,
        BookDTO reviewBook,
        String content) {
}