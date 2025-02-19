package com.example.demo.Order;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    Collection<Order> findAllByOrderUser();
}
