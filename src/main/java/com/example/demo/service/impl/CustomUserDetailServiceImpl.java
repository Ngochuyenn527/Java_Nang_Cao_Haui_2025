package com.example.demo.service.impl;

import com.example.demo.model.dto.MyUserDetail;
import com.example.demo.model.dto.RoleDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//load thông tin người dùng từ database để Spring Security dùng để đăng nhập (xu ly trong class SecurityConfig)
//UserDetailsService là interface cốt lõi trong Spring Security dùng để tải thông tin người dùng (user) từ hệ thống, phục vụ cho quá trình đăng nhập (authentication).
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUserNameAndStatus(name, 1);
        if (userDTO == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDTO role : userDTO.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
        }

        MyUserDetail myUserDetail = new MyUserDetail(
                name,
                userDTO.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );

        BeanUtils.copyProperties(userDTO, myUserDetail);
        return myUserDetail;
    }
}
