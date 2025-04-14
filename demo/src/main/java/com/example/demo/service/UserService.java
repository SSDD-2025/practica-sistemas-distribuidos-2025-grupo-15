package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public Collection<UserDTO> getUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO getUser(int id) {
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    public UserDTO getUser(String userName) {
        return toDTO(userRepository.findByUserName(userName).orElseThrow());
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = toDomain(userDTO);
        userRepository.save(user);
        return toDTO(user);
    }

    public UserDTO updateUser(int id, UserDTO userDTO) {
        if (userRepository.existsById(id)){
            User updateUser = toDomain(userDTO);
            updateUser.setId(id);
            userRepository.save(updateUser);
            return toDTO(updateUser);
        }
        else{
            throw new NoSuchElementException();
        }
    }

    public UserDTO deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
        return toDTO(user);
    }

    public boolean userExists(User user1) {
        if (userRepository.existsByUserName(user1.getUserName())) {
            return true;
        } else {
            return false;
        }
    }

    private UserDTO toDTO(User user){
        return userMapper.toDTO(user);
    }

    private User toDomain(UserDTO userDTO){
        return userMapper.toDomain(userDTO);
    }

    private Collection<UserDTO> toDTOs(Collection<User> users){
        return userMapper.toDTOs(users);
    }

}
