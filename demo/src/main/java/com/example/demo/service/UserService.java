package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Collection<User> getUsers() {
        Iterable<User> iterable = userRepository.findAll();
        Collection<User> users = new ArrayList<>();
        iterable.forEach(users::add);
        return users;
    }

    public User getUser(int id) {
        if (userRepository.existsById(id)) {
            return userRepository.findById(id).get();
        }
        return null;
    }

    public User getUser(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()){
            return user.get();
        } else{
            return null;
        }
    }

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.delete(userRepository.findById(id).get());
            return true;
        }
        return false;
    }

    public boolean userExists(User user1) {
        if (userRepository.existsByUserName(user1.getUserName())) {
            return true;
        } else {
            return false;
        }
    }

}
