package com.example.demo.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;


@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String showBooks(Model model, HttpSession session) {
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @GetMapping("/book/{ISBN}")
    public String showBook(@PathVariable int ISBN, Model model, HttpSession session) {
        Book book = bookService.getBook(ISBN);
        session.setAttribute("bookId", ISBN);
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviewService.getReviews(book));
        return "book";
       
    }

    @GetMapping("/book/{ISBN}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int ISBN) throws SQLException {
        Book book = bookService.getBook(ISBN);
        
        if (book != null && book.getImageFile() != null) {
            Blob image = book.getImageFile();
            InputStreamResource file = new InputStreamResource(image.getBinaryStream());

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length())
                .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
}

    
    @GetMapping("/newBook")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "newBook";
    }

    @PostMapping("/newBook")
    public String createBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile imageFile, Model model) throws IOException {
        
        if (bookService.getBook(book.getISBN()) != null) {
            model.addAttribute("error", "Error: El libro con ese ISBN ya existe.");
            return "newBook";  
        }

        if (!imageFile.isEmpty()) {
            book.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }

        bookService.createBook(book);
        model.addAttribute("success", "El libro ha sido añadido correctamente.");
        model.addAttribute("books", bookService.getBooks());  

        return "redirect:/";
    }

    @GetMapping("/editBook")
    public String editBook(@RequestParam int ISBN, Model model) {
        Book book = bookService.getBook(ISBN);
        
        if (book != null) {
            model.addAttribute("book", book);
            return "editBook";
        }

        model.addAttribute("error", "Error: El libro no existe.");
        model.addAttribute("books", bookService.getBooks());
        return "home"; 
    }
   
    @PostMapping("/saveEdit")
    public String saveEditedBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile imageFile, Model model) throws IOException{
        if (!imageFile.isEmpty()) {
            book.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }
        if (bookService.getBook(book.getISBN()) != null) {
            bookService.updateBook(book);
            model.addAttribute("success", "El libro ha sido actualizado correctamente.");
        } else {
            model.addAttribute("error", "Error: No se pudo actualizar el libro.");
        }

        model.addAttribute("books", bookService.getBooks());
        return "home"; 
}

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam int ISBN, Model model) {
        Book book = bookService.getBook(ISBN);
        
        if (book == null) {
            model.addAttribute("error", "Error: El libro no existe.");
        } else {
            bookService.deleteBook(ISBN);
            model.addAttribute("success", "El libro ha sido eliminado correctamente.");
        }

        model.addAttribute("books", bookService.getBooks()); 
        return "redirect:/"; 
    }

}

