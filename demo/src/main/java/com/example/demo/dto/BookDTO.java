package com.example.demo.dto;

public record BookDTO(
    Integer id,
    String title,
    String author,
    String synopsis,
    double price,
    int ISBN,
    String image 
) {}

