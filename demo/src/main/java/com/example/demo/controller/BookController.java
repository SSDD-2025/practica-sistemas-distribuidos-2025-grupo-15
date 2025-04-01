package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
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
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showBooks(Model model, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        User user = null;
        if (req != null){
            user = userService.getUser(req.getName());
        }
        model.addAttribute("books", bookService.getBooks());
        if(user != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }
        
        return "home";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable int id, Model model, HttpSession session, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        User user = null;
        if (req != null){
            user = userService.getUser(req.getName());
        }
        
        Book book = bookService.getBook(id);
        session.setAttribute("bookId", id);
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviewService.getReviews(book));
        if(user != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }

        return "book";

    }

    @GetMapping("book/image/{id}")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws SQLException {
        Book book = bookService.getBook(id);

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
    public String newBookForm(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);

        if (user == null) {
            return "errorNoSesionAddBook";
        }

        model.addAttribute("book", new Book());
        return "newBook";
    }

    @PostMapping("/newBook")
    public String createBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile imageFile, Model model)
            throws IOException {

        if (bookService.getBookByISBN(book.getISBN()) != null) {
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
    public String editBook(@RequestParam int id, HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);

        if (user == null) {
            model.addAttribute("ISBN", id);
            return "errorNoSesionEditBook";
        }

        Book book = bookService.getBook(id);

        if (book != null) {
            model.addAttribute("book", book);
            return "editBook";
        }

        model.addAttribute("error", "Error: El libro no existe.");
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @PostMapping("/saveEdit")
    public String saveEditedBook(@ModelAttribute Book book,
            @RequestParam(value = "image", required = false) MultipartFile imageFile, Model model) throws IOException {
        Book existingBook = bookService.getBook(book.getId());

        if (imageFile != null && !imageFile.isEmpty()) {
            book.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        } else {
            book.setImageFile(existingBook.getImageFile());
        }
        bookService.updateBook(book);
        model.addAttribute("success", "El libro ha sido actualizado correctamente.");
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam int id, HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        Book book = bookService.getBook(id);

        if (user == null) {
            model.addAttribute("ISBN", id);
            return "errorNoSessionDeleteBook";
        }
        if (book == null) {
            model.addAttribute("error", "Error: El libro no existe.");
        } else {
            bookService.deleteBook(id);
            model.addAttribute("success", "El libro ha sido eliminado correctamente.");
        }

        model.addAttribute("books", bookService.getBooks());
        return "redirect:/";
    }

}
