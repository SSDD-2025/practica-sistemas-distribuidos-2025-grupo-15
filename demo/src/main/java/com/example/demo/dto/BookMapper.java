package com.example.demo.dto;

import com.example.demo.model.Book;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDTO(Book book);

    List<BookDTO> toDTOs(Collection<Book> books);

    Book toDomain(BookDTO bookDTO);
}

