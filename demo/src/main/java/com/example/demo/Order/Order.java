package com.example.demo.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.User.User;
import com.example.demo.Book.Book;

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
    private List<Book> books = new ArrayList<>();

    private LocalDateTime date;
    private float totalPrice;
    private String state;
    
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
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public float getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    

    
    
}
