package com.example.demo.Reviews;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Book.Book;
import com.example.demo.User.User;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    

    public Review getReview(int id){
        return reviewRepository.findById(id).get();
    }

    public Collection<Review> getReviews(User user){
        return reviewRepository.findAllByUser(user);
    }

    public Collection<Review> getReviews(Book book){
        return reviewRepository.findAllByBook(book);
    }

    public Review createReview(Review review){
        return reviewRepository.save(review);
    }

    public boolean deleteReview(Review review){
        if (reviewRepository.existsById(review.getId())){
            reviewRepository.delete(reviewRepository.findById(review.getId()).get());
            return true;
        }
        return false;
    }

    public boolean updateReview(Review review){
        if (reviewRepository.existsById(review.getId())){
            reviewRepository.save(review);
            return true;
        }
        return false;
    }
}
