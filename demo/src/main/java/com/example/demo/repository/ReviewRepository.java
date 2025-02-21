package com.example.demo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Collection<Review> findAllByUser(User user);
    Collection<Review> findAllByBook(Book book);
}
