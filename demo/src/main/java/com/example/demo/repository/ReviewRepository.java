package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByUser(User user);
    List<Review> findAllByBook(Book book);
}
