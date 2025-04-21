package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.NoSuchElementException;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    private static final String IMAGE_DIR = "src/main/resources/images/";

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;


    @GetMapping("/")
    public Page<BookDTO> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int id) {
        BookDTO book = bookService.getBook(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        bookService.createBook(bookDTO);

        URI location = fromCurrentRequest().path("/book/{id}").buildAndExpand(bookDTO.id()).toUri();
        return ResponseEntity.created(location).body(bookDTO);
    }

    @PutMapping("/book/{id}")
    public BookDTO updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/book/{id}")
    public BookDTO deleteBook(@PathVariable int id) {
        BookDTO bookDTO = bookService.getBook(id); 
        if (bookDTO == null) {
            throw new NoSuchElementException();
        }
        return bookService.deleteBook(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
        BookDTO bookDTO = bookService.getBook(id);
        if (bookDTO == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            bookMapper.toDomain(bookDTO).setImageFile(new SerialBlob(file.getBytes()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error setting image blob.");
        }
        
        bookService.createBook(bookDTO);
        return ResponseEntity.ok("Image uploaded successfully.");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable int id) throws IOException {
        BookDTO book = bookService.getBook(id);
        if (book == null || bookMapper.toDomain(book).getImageFile() == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(IMAGE_DIR + bookMapper.toDomain(book).getImageFile());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private BookDTO toDTO(Book book){
        return bookMapper.toDTO(book);
    }
}
