package com.example.demo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    Collection<Order> findAllByOrderUser();
}
