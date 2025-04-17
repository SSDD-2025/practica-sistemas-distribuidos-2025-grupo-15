package com.example.demo.dto;

import java.sql.Blob;
import java.util.List;


public record BookDTO(
        Integer  id,
        String title,
        String author,
        String synopsis,
        double price,
        int ISBN,
        Blob imageFile) {

}
