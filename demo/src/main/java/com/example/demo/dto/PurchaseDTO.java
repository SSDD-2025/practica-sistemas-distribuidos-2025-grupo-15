package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseDTO(int id, UserDTO purchaseUser, LocalDateTime date, String state, List<BookDTO> books) {}