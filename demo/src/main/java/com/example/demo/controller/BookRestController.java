package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.NoSuchElementException;
import java.util.UUID;

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

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public Page<BookDTO> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(book -> {
            BookDTO dto = bookMapper.toDTO(book);
            String image = dto.image();
            if (image != null && !image.isEmpty()) {
                dto = new BookDTO(
                        dto.id(),
                        dto.title(),
                        dto.author(),
                        dto.synopsis(),
                        dto.price(),
                        dto.ISBN(),
                        "https://localhost:8443/api/books/images/" + image);
            }
            return dto;
        });
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int id) {
        BookDTO dto = bookService.getBook(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        String image = dto.image();
        if (image != null && !image.isEmpty()) {
            dto = new BookDTO(
                    dto.id(),
                    dto.title(),
                    dto.author(),
                    dto.synopsis(),
                    dto.price(),
                    dto.ISBN(),
                    "https://localhost:8443/api/books/images/" + image);
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/")
    public ResponseEntity<BookDTO> createBook(@RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("synopsis") String synopsis,
            @RequestParam("price") double price,
            @RequestParam("ISBN") int ISBN,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        BookDTO dto = new BookDTO(null, title, author, synopsis, price, ISBN, filename);
        BookDTO created = bookService.createBook(dto);

        BookDTO dtoWithUrl = new BookDTO(
                created.id(),
                created.title(),
                created.author(),
                created.synopsis(),
                created.price(),
                created.ISBN(),
                "https://localhost:8443/api/books/images/" + created.image());

        URI location = fromCurrentRequest().path("/book/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(dtoWithUrl);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable int id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("synopsis") String synopsis,
            @RequestParam("price") double price,
            @RequestParam("ISBN") int ISBN,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        BookDTO existing = bookService.getBook(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = existing.image();
        if (imageFile != null && !imageFile.isEmpty()) {
            filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.createDirectories(filePath.getParent());
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        BookDTO dto = new BookDTO(id, title, author, synopsis, price, ISBN, filename);
        BookDTO updated = bookService.updateBook(id, dto);

        BookDTO dtoWithUrl = new BookDTO(
                updated.id(),
                updated.title(),
                updated.author(),
                updated.synopsis(),
                updated.price(),
                updated.ISBN(),
                "https://localhost:8443/api/books/images/" + updated.image());

        return ResponseEntity.ok(dtoWithUrl);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable int id) {
        BookDTO bookDTO = bookService.getBook(id);
        if (bookDTO == null) {
            throw new NoSuchElementException();
        }

        BookDTO deleted= bookService.deleteBook(id);

        String image = deleted.image();
        if (image != null && !image.isEmpty()) {
            deleted = new BookDTO(
                    deleted.id(),
                    deleted.title(),
                    deleted.author(),
                    deleted.synopsis(),
                    deleted.price(),
                    deleted.ISBN(),
                    "https://localhost:8443/api/books/images/" + image);
        }

        return ResponseEntity.ok(deleted);

    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, filename);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(Files.probeContentType(path) != null
                ? MediaType.parseMediaType(Files.probeContentType(path))
                : MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private BookDTO toDTO(Book book) {
        return bookMapper.toDTO(book);
    }
}
