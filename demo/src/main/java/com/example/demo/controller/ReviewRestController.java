package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Review;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;


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

    @PostMapping("/")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = reviewMapper.toDomain(reviewDTO);
        UserDTO userDTO = userService.getUser(review.getReviewUser().getUserName());
        BookDTO bookDTO = bookService.getBook(review.getReviewBook().getId());

        if (userDTO == null || bookDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        review = new Review(userMapper.toDomain(userDTO), bookMapper.toDomain(bookDTO), reviewDTO.content());
        ReviewDTO newReviewDTO = reviewMapper.toDTO(review);
        reviewRepository.save(review);

        URI location = fromCurrentRequest().path("/review/{id}").buildAndExpand(review.getId()).toUri();
        return ResponseEntity.created(location).body(newReviewDTO);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        ReviewDTO reviewDTO = reviewService.getReview(id);
        if (reviewDTO == null) {
            return ResponseEntity.notFound().build();
        }

        reviewService.deleteReview(reviewDTO.id());
        return ResponseEntity.noContent().build();
    }

    private ReviewDTO toDTO(Review review){
        return reviewMapper.toDTO(review);
    }


}
