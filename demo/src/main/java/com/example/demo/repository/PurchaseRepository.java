package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Purchase;
import com.example.demo.model.User;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByPurchaseUser(User user);

    Page<Purchase> findByPurchaseUser_Id(int id, Pageable pageable);

    Optional<Purchase> findById(int id);
}
