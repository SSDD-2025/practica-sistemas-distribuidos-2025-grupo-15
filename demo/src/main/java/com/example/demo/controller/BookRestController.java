package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.sql.Blob;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private static final String IMAGE_DIR = "src/main/resources/images/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;
    

    @GetMapping("/")
    public Collection<BookDTO> getBooks() {
        return bookRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int id) {
        BookDTO book = bookService.getBook(id);
        if (book != null) {
            return ResponseEntity.ok(toDTO(book));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        Book book = toDomain(bookDTO);
        bookRepository.save(book);

        URI location = fromCurrentRequest().path("/book/{id}").buildAndExpand(book.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(book));
    }

    @PutMapping("/book/{id}")
    public BookDTO updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        if (bookRepository.existsById(id)) {
            Book updatedBook = toDomain(bookDTO);
            updatedBook.setId(id);
            bookRepository.save(updatedBook);
            return toDTO(updatedBook);
        } else {
            throw new NoSuchElementException();
        }
    }

    @DeleteMapping("/book/{id}")
    public BookDTO deleteBook(@PathVariable int id) {
        Book book = bookRepository.findById(id); 
        if (book == null) {
            throw new NoSuchElementException();
        }
        bookRepository.deleteById(id);
        return toDTO(book);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
        Book book = bookService.getBook(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            book.setImageFile(new SerialBlob(file.getBytes()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error setting image blob.");
        }
        
        bookRepository.save(book);
        return ResponseEntity.ok("Image uploaded successfully.");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable int id) throws IOException {
        Book book = bookService.getBook(id);
        if (book == null || book.getImageFile() == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(IMAGE_DIR + book.getImageFile());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private BookDTO toDTO(Book book) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getSynopsis(),
            book.getPrice(),
            book.getISBN(),
            book.getImageFile(),
            book.getBookReviews()
        );
    }

    private Book toDomain(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.id());
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setSynopsis(dto.synopsis());
        book.setPrice(dto.price());
        book.setISBN(dto.ISBN());
        book.setImageFile(dto.imageFile());
        book.setBookReviews(dto.bookReviews());
        return book;
    }
}
