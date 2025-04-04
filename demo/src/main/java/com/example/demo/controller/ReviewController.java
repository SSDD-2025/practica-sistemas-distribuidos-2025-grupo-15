package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;
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

    @GetMapping("/newReview")
    public String newReview(HttpSession session, Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (user == null) {
            model.addAttribute("ISBN", bookId);
            return "errorNoSessionAddReview";
        }
        if (bookId == null) {
            return "error";
        }

        model.addAttribute("userName", name);
        model.addAttribute("title", bookService.getBook(bookId).getTitle());
        return "newReview";
    }

    @PostMapping("/newReview")
    public String newReviewProcess(@RequestParam String content, HttpSession session, Model model, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (user == null) {
            return "redirect:/home";
        }
        if (bookId == null) {
            return "redirect:/home";
        }
        
        Book book = bookService.getBook(bookId);

        Review newReview = new Review(user, book, content);
        book.addReview(newReview);

        bookService.updateBook(book);
        return "redirect:/";
    }

    @GetMapping("/myReviews")
    public String myReviews(HttpServletRequest request, Model model) {
        String name = request.getUserPrincipal().getName();
        User user = userService.getUser(name);
        
        if (user != null) {
            model.addAttribute("reviews", reviewService.getReviews(user));
        }
    
        return "myReviews";
    }

    @PostMapping("/deleteReview/{id}")
    public String deleteReview(@PathVariable int id) {
        Review review = reviewService.getReview(id);
        if (review == null) {
            return "error";
        }
        reviewService.deleteReview(review);
        return "redirect:/myReviews";
    }

}
