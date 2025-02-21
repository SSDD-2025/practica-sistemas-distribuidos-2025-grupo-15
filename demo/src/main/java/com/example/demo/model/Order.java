package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User orderUser;

    @ManyToMany
    private Map<Book, Integer> booksAndAmount = new ConcurrentHashMap<>();

    private LocalDateTime date;
    private String state;

    public Order(){}
    
    public Order(User user, Map<Book,Integer> bookList, LocalDateTime date, String state){
        super();
        this.orderUser = user;
        this.booksAndAmount = bookList;
        this.date = date;
        this.state = state;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getOrderUser() {
        return orderUser;
    }
    public void setOrderUser(User orderUser) {
        this.orderUser = orderUser;
    }
    public Map<Book, Integer> getBooksAndAmount() {
        return booksAndAmount;
    }
    public void setBooksAndAmount(Map<Book, Integer> booksAndAmount) {
        this.booksAndAmount = booksAndAmount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }  
}
