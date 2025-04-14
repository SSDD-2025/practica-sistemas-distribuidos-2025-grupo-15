package com.example.demo.model;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.UserDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User reviewUser;

    @ManyToOne
    private Book reviewBook;

    private String content;

    public Review() {
    }

    public Review(UserDTO user, BookDTO book, String content) {
        super();
        this.reviewUser = user;
        this.reviewBook = book;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }

    public Book getReviewBook() {
        return reviewBook;
    }

    public void setReviewBook(Book reviewBook) {
        this.reviewBook = reviewBook;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
