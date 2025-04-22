package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String author;
    private String synopsis;
    private double price;
    private int ISBN;
    private String image; // nombre del archivo de imagen

    @OneToMany(mappedBy = "reviewBook", cascade = CascadeType.ALL)
    private List<Review> bookReviews;

    public Book() {
    }

    public Book(int ISBN, String title, String author, String synopsis, double price, String image) {
        super();
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.price = price;
        this.image = image;
    }

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

    public void addReview(Review review) {
        this.bookReviews.add(review);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
