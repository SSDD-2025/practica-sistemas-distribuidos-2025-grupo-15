package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public Collection<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{username}")
    public Collection<ReviewDTO> getReviewsByUser(@PathVariable String username) {
        User user = userService.getUser(username);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return reviewService.getReviews(user).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable int id) {
        Review review = reviewService.getReview(id);
        if (review != null) {
            return ResponseEntity.ok(toDTO(review));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO dto) {
        User user = userService.getUser(dto.reviewUser().getUserName());
        Book book = bookService.getBook(dto.reviewBook().getId());

        if (user == null || book == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Review review = new Review(user, book, dto.content());
        reviewRepository.save(review);

        URI location = fromCurrentRequest().path("/review/{id}").buildAndExpand(review.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(review));
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        Review review = reviewService.getReview(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        reviewService.deleteReview(review);
        return ResponseEntity.noContent().build();
    }

    private ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getReviewUser(),
                review.getReviewBook(),
                review.getContent()
        );
    }
}
