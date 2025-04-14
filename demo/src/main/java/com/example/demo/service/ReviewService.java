package com.example.demo.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookMapper;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BookMapper bookMapper;

    public ReviewDTO getReview(int id) {
        return toDTO(reviewRepository.findById(id).orElseThrow());
    }

    public Collection<ReviewDTO> getReviews(UserDTO userDTO) {
        return toDTOs(reviewRepository.findAllByReviewUser(userMapper.toDomain(userDTO)));
    }

    public Collection<ReviewDTO> getReviews(BookDTO bookDTO) {
        return toDTOs(reviewRepository.findAllByReviewBook(bookMapper.toDomain(bookDTO)));
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = toDomain(reviewDTO);
        reviewRepository.save(review);
        return toDTO(review);
    }

    public ReviewDTO deleteReview(int id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.deleteById(id);
        return toDTO(review);
    }

    public ReviewDTO updateReview(int id, ReviewDTO reviewDTO) {
        if (reviewRepository.existsById(id)) {
            Review updateReview = toDomain(reviewDTO);
            updateReview.setId(id);
            reviewRepository.save(updateReview);
            return toDTO(updateReview);
        } else {
            throw new NoSuchElementException();
        }
    }

    private ReviewDTO toDTO(Review review) {
        return reviewMapper.toDTO(review);
    }

    private Review toDomain(ReviewDTO reviewDTO) {
        return reviewMapper.toDomain(reviewDTO);
    }

    private Collection<ReviewDTO> toDTOs(Collection<Review> reviews) {
        return reviewMapper.toDTOs(reviews);
    }
}
