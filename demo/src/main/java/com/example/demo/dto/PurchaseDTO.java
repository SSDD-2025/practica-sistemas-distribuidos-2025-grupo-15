package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Book;
import com.example.demo.model.User;

public record PurchaseDTO(int id, User purchaseUser, List<Book> books, LocalDateTime date, String state) {}