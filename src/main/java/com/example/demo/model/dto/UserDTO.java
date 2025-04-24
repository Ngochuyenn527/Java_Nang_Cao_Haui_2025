package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractDTO {
    private String userName;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private Integer status;
    private String roleCode;

    private List<RoleDTO> roles = new ArrayList<>();
}

