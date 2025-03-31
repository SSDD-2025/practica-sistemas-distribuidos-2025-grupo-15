package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    private String encodedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "reviewUser", cascade = CascadeType.ALL)
    private List<Review> userReviews = new ArrayList<>();

    @OneToMany(mappedBy = "purchaseUser", cascade = CascadeType.ALL)
    private List<Purchase> userPurchases = new ArrayList<>();

    public User() {
    }

    public User(String userName, String encodedPassword, List<String> roles) {
        super();
        this.userName = userName;
        this.encodedPassword = encodedPassword;
        this.roles = roles;
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

    public String getencodedPassword() {
        return encodedPassword;
    }

    public void setencodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
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

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    
}
