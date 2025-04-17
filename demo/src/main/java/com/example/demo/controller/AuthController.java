
package com.example.demo.controller;

import com.example.demo.security.JwtUtil;
import com.example.demo.security.LoginRequest;
import com.example.demo.security.LoginResponse;
import com.example.demo.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return new LoginResponse(jwt);
    }
}
