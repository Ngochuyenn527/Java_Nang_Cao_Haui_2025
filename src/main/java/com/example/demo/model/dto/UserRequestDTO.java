package com.example.demo.model.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String userName;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String otp;
}