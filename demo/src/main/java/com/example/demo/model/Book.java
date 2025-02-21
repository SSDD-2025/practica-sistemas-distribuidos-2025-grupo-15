package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ISBN;

    private String title;
    private String author;
    private String synopsis;
    private double price;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Review> bookReviews;

    // Constructor vacío
    public Book() {}

    // Constructor con parámetros
    public Book(int ISBN, String title, String author, String synopsis, double price) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.price = price;
    }

    // Getters y Setters
    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getPrice() { 
        return price; 
    }
    public void setPrice(double price) {
         this.price = price; 
    }

    public List<Review> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<Review> bookReviews) {
        this.bookReviews = bookReviews;
    }
}

