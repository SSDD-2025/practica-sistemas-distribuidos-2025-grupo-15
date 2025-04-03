package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Purchase;
import com.example.demo.model.Review;

public record UserDTO(String userName, String encondedPassword, int id, List<String> roles, List<Review> userReviews, List<Purchase> userPurchases) {}