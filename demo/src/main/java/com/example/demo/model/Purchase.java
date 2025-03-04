package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User purchaseUser;

    @ManyToMany
    private List<Book> books = new ArrayList<>();

    private LocalDateTime date;
    private String state;

    public Purchase() {
    }

    public Purchase(User user, List<Book> bookList, LocalDateTime date, String state) {
        super();
        this.purchaseUser = user;
        this.books = bookList;
        this.date = date;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPurchaseUser() {
        return purchaseUser;
    }

    public void setPurchaseUser(User purchaseUser) {
        this.purchaseUser = purchaseUser;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }
}
