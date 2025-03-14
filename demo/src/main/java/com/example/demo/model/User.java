package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userName;
    private String password;

    @OneToMany(mappedBy = "reviewUser", cascade = CascadeType.ALL)
    private List<Review> userReviews = new ArrayList<>();

    @OneToMany(mappedBy = "purchaseUser", cascade = CascadeType.ALL)
    private List<Purchase> userPurchases = new ArrayList<>();

    public User() {
    }

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Review> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<Review> userReviews) {
        this.userReviews = userReviews;
    }

    public List<Purchase> getUserPurchases() {
        return userPurchases;
    }

    public void setUserPurchases(List<Purchase> userPurchases) {
        this.userPurchases = userPurchases;
    }

}
