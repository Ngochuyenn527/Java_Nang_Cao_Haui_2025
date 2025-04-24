package com.example.demo.service;

import com.example.demo.config.MyExceptionConfig;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(long id);

    UserDTO getUserByUserNameAndStatus(String name, int status);

    List<UserDTO> getUsersByRoleCode(String roleCode);

    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void updatePassword(long id, PasswordDTO userDTO) throws MyExceptionConfig;

    UserDTO resetPassword(long id);

    void deleteUser(Long id);
}

