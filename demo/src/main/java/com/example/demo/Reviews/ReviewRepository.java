package com.example.demo.Reviews;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Book.Book;
import com.example.demo.User.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Collection<Review> findAllByUser(User user);
    Collection<Review> findAllByBook(Book book);
}
