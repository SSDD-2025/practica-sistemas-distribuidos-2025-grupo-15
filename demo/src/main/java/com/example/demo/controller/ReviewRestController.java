package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

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

    @Autowired
    private ReviewMapper reviewMapper;

    @GetMapping("/")
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(this::toDTO);
    }

    @GetMapping("/user/{username}")
    public Collection<ReviewDTO> getReviewsByUser(@PathVariable String username) {
        UserDTO userDTO = userService.getUser(username);
        if (userDTO == null) {
            throw new NoSuchElementException("User not found");
        }
        return reviewService.getReviews(userDTO);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable int id) {
        ReviewDTO reviewDTO = reviewService.getReview(id);
        if (reviewDTO != null) {
            return ResponseEntity.ok(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{username}/book/{id}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable String username, @PathVariable int id,
            @RequestBody ReviewDTO reviewDTO) {
        Review review = reviewMapper.toDomain(reviewDTO);
        User user = userService.getDomainUser(username);
        Book book = bookService.getDomainBook(id);

        if (user == null || book == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        review = new Review(user, book, reviewDTO.content());

        review = reviewRepository.save(review);
        ReviewDTO newReviewDTO = reviewMapper.toDTO(review);

        URI location = fromCurrentRequest().path("/review/{id}").buildAndExpand(review.getId()).toUri();
        return ResponseEntity.created(location).body(newReviewDTO);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable int id) {
        ReviewDTO reviewDTO = reviewService.getReview(id);
        if (reviewDTO == null) {
            return ResponseEntity.notFound().build();
        }

        reviewService.deleteReview(reviewDTO.id());
        return ResponseEntity.ok(reviewDTO);
    }

    private ReviewDTO toDTO(Review review) {
        return reviewMapper.toDTO(review);
    }

}
