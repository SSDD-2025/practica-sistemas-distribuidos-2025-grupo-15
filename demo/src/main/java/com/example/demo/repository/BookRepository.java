package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByAuthor (String author );
    Book findByTitle (String title );
    Book findByISBN(int ISBN);
}
