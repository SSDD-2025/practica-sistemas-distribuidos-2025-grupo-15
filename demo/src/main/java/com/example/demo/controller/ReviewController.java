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

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
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
    public String newReview(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (userId == null){
            return "error";
        }
        if (bookId == null){
            return "error";
        }

        model.addAttribute("userName", userService.getUser(userId).getUserName());
        model.addAttribute("title", bookService.getBook(bookId).getTitle());
        return "newReview";
    }

    @PostMapping("/newReview")
    public String newReviewProcess(@RequestParam String content, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        Integer bookId = (Integer) session.getAttribute("bookId");

        if (userId == null){
            return "redirect:/home";
        }
        if (bookId == null){
            return "redirect:/home";
        }

        User user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);

        Review newReview = new Review(user, book, content);

        reviewService.createReview(newReview);
        return "redirect:/home";
    }

    @GetMapping("/myReviews")
    public String myReviews(HttpSession session, Model model){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null){
            User user = userService.getUser(userId);
            if (user != null){
                model.addAttribute("reviews", reviewService.getReviews(user));
            }
        }
        return "myReviews";
    }
    
    
}
