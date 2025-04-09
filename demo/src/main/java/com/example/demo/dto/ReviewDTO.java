package com.example.demo.dto;

import com.example.demo.model.Book;
import com.example.demo.model.User;

public record ReviewDTO(
        int id,
        User reviewUser,
        Book reviewBook,
        String content) {
}