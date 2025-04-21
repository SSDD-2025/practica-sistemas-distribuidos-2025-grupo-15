package com.example.demo.dto;

import org.mapstruct.Mapper;

import com.example.demo.model.Book;

import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toDTO(Book book);

    List<BookDTO> toDTOs(Collection<Book> books);

    Book toDomain(BookDTO bookDTO);
}