package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Purchase;
import com.example.demo.model.User;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    
    List<Purchase> findAllByPurchaseUser(User user);
    Purchase findById(int id);
}
