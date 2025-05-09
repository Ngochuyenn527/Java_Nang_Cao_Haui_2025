package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public UserDTO login(@RequestBody LoginRequest request) {
        UserEntity user = userRepository.findOneByUserNameAndStatus(request.getUsername(), 1);
        if (user == null) {
            throw new RuntimeException("Sai username hoặc tài khoản bị khóa.");
        }

        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new RuntimeException("Sai mật khẩu.");
        }

        return userService.getUserByUserNameAndStatus(request.getUsername(), 1);
    }
}

