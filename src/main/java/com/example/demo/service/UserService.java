package com.example.demo.service;

import com.example.demo.config.MyExceptionConfig;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsersByStatus(int status);

    UserDTO getUserById(Long id);

    UserDTO getUserByUserNameAndStatus(String name, int status);

    List<UserDTO> getUsersByRoleCode(String roleCode);

    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void updatePassword(Long id, PasswordDTO userDTO) throws MyExceptionConfig;

    UserDTO resetPassword(Long id);

    void deleteUser(Long id);
}

