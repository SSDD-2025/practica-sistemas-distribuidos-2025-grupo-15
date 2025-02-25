package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public Collection<Book>getBooks(){
        Iterable<Book> books = bookRepository.findAll();
        Collection<Book> bookList = new ArrayList<>();
        books.forEach(bookList::add);
        return bookList;
    }

    public Book getBook(int ISBN){     
        return bookRepository.findById(ISBN).get();
    }

    public Book getBook(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBooks(String author) {
        return bookRepository.findByAuthor(author);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public boolean updateBook(Book book) {
        if (bookRepository.existsById(book.getISBN())) {
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public boolean deleteBook(int ISBN) {
        if (bookRepository.existsById(ISBN)) {
            bookRepository.deleteById(ISBN);
            return true;
        }
        return false;
    }
}

