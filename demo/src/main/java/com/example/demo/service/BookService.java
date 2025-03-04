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

    @Autowired
    PurchaseService purchaseService;

    public Collection<Book> getBooks() {
        Iterable<Book> books = bookRepository.findAll();
        Collection<Book> bookList = new ArrayList<>();
        books.forEach(bookList::add);
        return bookList;
    }

    public Book getBookByISBN(int ISBN) {
        return bookRepository.findByISBN(ISBN);
    }

    public Book getBook(int id) {
        return bookRepository.findById(id);
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
        if (bookRepository.existsById(book.getId())) {
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public boolean deleteBook(int id) {
        if (bookRepository.existsById(id)) {
            Book book = this.getBook(id);
            purchaseService.quitBookFromPurchases(book);
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
