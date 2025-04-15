package com.example.demo.dto;

import org.mapstruct.Mapper;

import com.example.demo.model.User;

import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    List<UserDTO> toDTOs(Collection<User> users);
    User toDomain(UserDTO userDTO);
}

