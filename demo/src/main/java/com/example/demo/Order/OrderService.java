package com.example.demo.Order;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.User.User;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    
    public Collection<Order> getOrders(){
        return orderRepository.findAll();
    }

    public Order getOrder(int id){
        return orderRepository.findById(id).get();
    }

    public Collection<Order> getOrders(User user){
        return orderRepository.findAllByOrderUser();
    }

    public Order createOrder(Order order){
        return orderRepository.save(order);
    }

    public boolean deleteReview(Order order){
        if (orderRepository.existsById(order.getId())){
            orderRepository.delete(orderRepository.findById(order.getId()).get());
            return true;
        }
        return false;
    }

    public boolean updateReview(Order order){
        if (orderRepository.existsById(order.getId())){
            orderRepository.save(order);
            return true;
        }
        return false;
    } 
}
