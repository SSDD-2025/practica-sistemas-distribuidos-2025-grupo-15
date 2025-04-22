package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.nio.file.*;

@Controller
public class BookController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showBooks(Model model, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        if (req != null) {
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable int id, Model model, HttpSession session, HttpServletRequest request) {
        Principal req = request.getUserPrincipal();
        if (req != null) {
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }

        BookDTO bookDTO = bookService.getBook(id);
        session.setAttribute("bookId", id);
        model.addAttribute("book", bookDTO);
        model.addAttribute("reviews", reviewService.getReviews(bookDTO));

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", token);

        return "book";
    }

    @GetMapping("/newBook")
    public String newBookForm(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        if (userDTO == null) {
            return "errorNoSesionAddBook";
        }
        model.addAttribute("book", new Book());
        return "newBook";
    }

    @PostMapping("/newBook")
    public String createBook(@RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("synopsis") String synopsis,
            @RequestParam("price") double price,
            @RequestParam("ISBN") int ISBN,
            @RequestParam("image") MultipartFile imageFile,
            Model model) throws IOException {

        if (bookService.getBookByISBN(ISBN) != null) {
            model.addAttribute("error", "Error: El libro con ese ISBN ya existe.");
            return "newBook";
        }

        String filename = null;
        if (!imageFile.isEmpty()) {
            filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get("uploads", filename);
            Files.createDirectories(filePath.getParent());
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        BookDTO dto = new BookDTO(null, title, author, synopsis, price, ISBN, filename);
        bookService.createBook(dto);

        model.addAttribute("success", "El libro ha sido añadido correctamente.");
        model.addAttribute("books", bookService.getBooks());

        return "redirect:/";
    }

    @GetMapping("/editBook")
    public String editBook(@RequestParam int id, HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        if (userDTO == null) {
            model.addAttribute("ISBN", id);
            return "errorNoSesionEditBook";
        }

        BookDTO bookDTO = bookService.getBook(id);
        if (bookDTO != null) {
            model.addAttribute("book", bookDTO);
            return "editBook";
        }

        model.addAttribute("error", "Error: El libro no existe.");
        model.addAttribute("books", bookService.getBooks());
        return "home";
    }

    @PostMapping("/saveEdit")
    public String saveEditedBook(@RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("synopsis") String synopsis,
            @RequestParam("price") double price,
            @RequestParam("ISBN") int ISBN,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Model model) throws IOException {

        String filename = bookService.getBook(id).image();

        if (imageFile != null && !imageFile.isEmpty()) {
            filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get("uploads", filename);
            Files.createDirectories(filePath.getParent());
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        BookDTO updated = new BookDTO(id, title, author, synopsis, price, ISBN, filename);
        bookService.updateBook(id, updated);

        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam int id,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("error", "Error: No tienes sesión activa.");
            return "redirect:/errorNoSessionDeleteBook";
        }

        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Libro eliminado correctamente.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Error: El libro no existe.");
        }

        return "redirect:/";
    }
}
