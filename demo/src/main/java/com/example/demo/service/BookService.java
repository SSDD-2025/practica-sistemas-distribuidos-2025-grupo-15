package com.example.demo.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    BookMapper bookMapper;

    public Collection<BookDTO> getBooks() {
        return toDTOs(bookRepository.findAll());
    }

    public BookDTO getBookByISBN(int ISBN) {
        return toDTO(bookRepository.findByISBN(ISBN).orElseThrow());
    }

    public BookDTO getBook(int id) {
        return toDTO(bookRepository.findByISBN(id).orElseThrow());
    }

    public BookDTO getBook(String title) {
        return toDTO(bookRepository.findByTitle(title).orElseThrow());
    }

    public Collection<BookDTO> getBooks(String author) {
        return toDTOs(bookRepository.findByAuthor(author));
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = toDomain(bookDTO);
        bookRepository.save(book);
        return toDTO(book);
    }

    public BookDTO updateBook(int id, BookDTO bookDTO) {
        if (bookRepository.existsById(id)){
            Book updateBook = toDomain(bookDTO);
            updateBook.setId(id);
            bookRepository.save(updateBook);
            return toDTO(updateBook);
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public BookDTO deleteBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow();
        purchaseService.quitBookFromPurchases(book);
        bookRepository.deleteById(id);
        return toDTO(book);
    }

    private BookDTO toDTO(Book book){
        return bookMapper.toDTO(book);
    }

    private Book toDomain(BookDTO bookDTO){
        return bookMapper.toDomain(bookDTO);
    }

    private Collection<BookDTO> toDTOs(Collection<Book> books){
        return bookMapper.toDTOs(books);
    }
}
