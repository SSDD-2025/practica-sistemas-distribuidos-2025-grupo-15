package com.example.demo.dto;

import java.sql.Blob;
import java.util.List;

import com.example.demo.model.Review;

public record BookDTO(
        int id,
        String title,
        String author,
        String synopsis,
        double price,
        int ISBN,
        Blob imageFile,
        List<Review> bookReviews) {

}
