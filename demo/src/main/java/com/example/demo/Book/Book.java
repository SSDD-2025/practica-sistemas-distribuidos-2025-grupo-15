package com.example.demo.Book;

import jakarta.persistence.*;
import java.util.List;
import com.example.demo.Reviews.Review;

@Entity
public class Book {
    
    @Id
    private int ISBN;

    private String title;
    private String author;
    private String synopsis;

    private List<Review> bookReviews;

    // Constructor vacío
    public Book() {}

    // Constructor con parámetros
    public Book(int ISBN, String title, String author, String synopsis) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
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

    public List<Review> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<Review> bookReviews) {
        this.bookReviews = bookReviews;
    }
}

