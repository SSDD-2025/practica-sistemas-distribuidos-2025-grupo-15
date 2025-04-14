package com.example.demo.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping("/")
    public Collection<UserDTO> getUsers() {
      return userService.getUsers();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
       return userService.getUser(id);
    }

    // Crear un nuevo usuario
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        userDTO = userService.createUser(userDTO);
        URI location = fromCurrentRequest().path("/user/{id}").buildAndExpand(userDTO.id()).toUri();
        return ResponseEntity.created(location).body(userDTO); 
    }

    // Actualizar un usuario por ID
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable int id, @RequestBody UserDTO updatedUserDTO) {
        return userService.updateUser(id, updatedUserDTO);
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable int id) {
      return userService.deleteUser(id);
    }
}

