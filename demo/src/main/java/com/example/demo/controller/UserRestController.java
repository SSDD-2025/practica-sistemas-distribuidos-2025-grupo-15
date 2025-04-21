package com.example.demo.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    UserRestController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public Page<UserDTO> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        userDTO = userService.createUser(userDTO, passwordEncoder.encode(userDTO.encodedPassword()));
        URI location = fromCurrentRequest().path("/user/{id}").buildAndExpand(userDTO.id()).toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable int id, @RequestBody UserDTO updatedUserDTO) {

        return userService.updateUser(id, updatedUserDTO, passwordEncoder.encode(updatedUserDTO.encodedPassword()));
    }

    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    private UserDTO toDTO(User user) {
        return userMapper.toDTO(user);
    }
}
