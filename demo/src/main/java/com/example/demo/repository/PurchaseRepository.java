package com.example.demo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    
    Collection<Purchase> findAllByPurchaseUser();
}
