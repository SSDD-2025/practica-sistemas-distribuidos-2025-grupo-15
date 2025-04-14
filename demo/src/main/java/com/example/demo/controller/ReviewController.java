package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.UserDTO;

import com.example.demo.model.Book;
import com.example.demo.model.Review;

import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;



    @GetMapping("/newReview")
    public String newReview(HttpSession session, Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (userDTO == null) {
            model.addAttribute("ISBN", bookId);
            return "errorNoSessionAddReview";
        }
        if (bookId == null) {
            return "error";
        }
        BookDTO bookDTO = bookService.getBook(bookId);
        Book book = bookMapper.toDomain(bookDTO);

        model.addAttribute("userName", name);
        model.addAttribute("title", book.getTitle());
        return "newReview";
    }

    @PostMapping("/newReview")
    public String newReviewProcess(@RequestParam String content, HttpSession session, Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (userDTO == null) {
            return "redirect:/home";
        }
        if (bookId == null) {
            return "redirect:/home";
        }
        
        BookDTO bookDTO = bookService.getBook(bookId);
        Book book = bookMapper.toDomain(bookDTO);
        Review newReview = new Review(userDTO, bookDTO, content);
        book.addReview(newReview);

        bookService.updateBook(bookId, bookDTO);
        return "redirect:/";
    }

    @GetMapping("/myReviews")
    public String myReviews(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        UserDTO userDTO = userService.getUser(name);
        
        if (userDTO != null) {
            model.addAttribute("reviews", reviewService.getReviews(userDTO));
        }
    
        return "myReviews";
    }

    @PostMapping("/deleteReview/{id}")
    public String deleteReview(@PathVariable int id) {
        ReviewDTO reviewDTO = reviewService.getReview(id);
        if (reviewDTO == null) {
            return "error";
        }
        reviewService.deleteReview(id);
        return "redirect:/myReviews";
    }

}
