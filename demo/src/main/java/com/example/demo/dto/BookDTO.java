package com.example.demo.dto;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record BookDTO(
        Integer id,
        String title,
        String author,
        String synopsis,
        double price,
        int ISBN,
        @JsonIgnore Blob imageFile) {
}
