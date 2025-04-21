package com.example.demo.dto;

import org.mapstruct.Mapper;

import com.example.demo.model.Review;

import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toDTOs(Collection<Review> reviews);

    Review toDomain(ReviewDTO reviewDTO);
}