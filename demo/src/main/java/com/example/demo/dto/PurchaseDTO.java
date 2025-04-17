package com.example.demo.dto;

import java.time.LocalDateTime;

public record PurchaseDTO(int id, LocalDateTime date, String state) {}