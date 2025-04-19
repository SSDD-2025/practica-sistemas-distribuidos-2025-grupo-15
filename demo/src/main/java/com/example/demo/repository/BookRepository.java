package com.example.demo.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Collection<Book> findByAuthor(String author);

    Optional<Book> findByTitle(String title);

    Optional<Book> findByISBN(int ISBN);

    Optional<Book> findById(int id);

    // Método para obtener libros con paginación
    Page<Book> findAll(org.springframework.data.domain.Pageable pageable);
}
