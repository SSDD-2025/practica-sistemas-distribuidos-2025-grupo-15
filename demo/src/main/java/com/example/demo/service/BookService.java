package com.example.demo.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
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
        return toDTO(bookRepository.findByISBN(ISBN).orElse(null));
    }

    public BookDTO getBook(int id) {
        return toDTO(bookRepository.findById(id).orElse(null));
    }

    public BookDTO getBook(String title) {
        return toDTO(bookRepository.findByTitle(title).orElse(null));
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
        System.out.println("Buscando libro con ID: " + id);
        Book book = bookRepository.findById(id).orElse(null);
    
        System.out.println("Quitando libro de compras");
        purchaseService.quitBookFromPurchases(book);
    
        System.out.println("Eliminando libro del repositorio");
        bookRepository.deleteById(id);
    
        return toDTO(book);
    }

    // Obtiene un libro de dominio por ID
    public Book getDomainBook(int bookId) {
        return bookRepository.findById(bookId).orElseThrow();
    }

    // Obtiene las reseñas de un libro por ID
    public List<Review> getBookReviews(int id) {
        Book book = bookRepository.findById(id).orElseThrow();
        return book.getBookReviews();
    }

    // Métodos auxiliares para convertir entre DTO y dominio
    private BookDTO toDTO(Book book){
        return bookMapper.toDTO(book);
    }

    private Book toDomain(BookDTO bookDTO){
        return bookMapper.toDomain(bookDTO);
    }

    private Collection<BookDTO> toDTOs(Collection<Book> books){
        return bookMapper.toDTOs(books);
    }

     //AJAXS  
    // Método para obtener una página de libros
    public Page<BookDTO> getBooksPage(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDTO);
    }

}

