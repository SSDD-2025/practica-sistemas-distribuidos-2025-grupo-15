package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping("/newBook")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "newBook";
    }

    @PostMapping("/newBook")
    public String createBook(@ModelAttribute Book book, Model model) {
        if (bookService.getBook(book.getISBN()) != null) {
            model.addAttribute("error", "Error: El libro con ese ISBN ya existe.");
        } else {
            bookService.createBook(book);
            model.addAttribute("success", "El libro ha sido añadido correctamente.");
        }

        model.addAttribute("books", bookService.getBooks()); 
        return "redirect:/";
    }

    @GetMapping("/editBook")
    public String editBook(@RequestParam int ISBN, Model model) {
        Book book = bookService.getBook(ISBN);
        
        if (book != null) {
            model.addAttribute("book", book);
            return "newBook";
        }

        model.addAttribute("error", "Error: El libro no existe.");
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

