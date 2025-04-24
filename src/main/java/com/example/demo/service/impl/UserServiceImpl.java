package com.example.demo.service.impl;

import com.example.demo.config.MyExceptionConfig;
import com.example.demo.constant.SystemConstant;
import com.example.demo.converter.UserConverter;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.custom.UserRepositoryCustom;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;


    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.getAllUsers();
        List<UserDTO> results = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDTO userDTO = userConverter.convertToUserDto(userEntity);
            userDTO.setRoleCode(userEntity.getRoles().get(0).getCode());
            results.add(userDTO);
        }
        return results;
    }


    @Override
    public UserDTO getUserById(long id) {
        UserEntity entity = userRepository.findById(id).get();
        List<RoleEntity> roles = entity.getRoles();
        UserDTO dto = userConverter.convertToUserDto(entity);
        roles.forEach(item -> {
            dto.setRoleCode(item.getCode());
        });
        return dto;
    }


    @Override
    public UserDTO getUserByUserNameAndStatus(String name, int status) {
        return userConverter.convertToUserDto(userRepository.findOneByUserNameAndStatus(name, status));
    }


    @Override
    public List<UserDTO> getUsersByRoleCode(String roleCode) {
        List<UserEntity> entities = userRepository.findByRoleCode(roleCode);
        return entities.stream().map(userConverter::convertToUserDto).toList();
    }


    @Override
    @Transactional
    public UserDTO addUser(UserDTO newUser) {
        RoleEntity role = roleRepository.findOneByCode(newUser.getRoleCode()); //get role
        UserEntity userEntity = userConverter.convertToUserEntity(newUser);
        userEntity.setRoles(Stream.of(role).collect(Collectors.toList())); //add role vao list role cua user đó
        userEntity.setStatus(1);
        // ✅ Mã hóa mật khẩu mặc định trước khi lưu (mặc định pass = 123456)
        userEntity.setPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));
        return userConverter.convertToUserDto(userRepository.save(userEntity));
    }


    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO updateUser) {
        UserEntity oldUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoleEntity role = roleRepository.findOneByCode(updateUser.getRoleCode());

        // Map lại entity từ DTO
        UserEntity userEntity = userConverter.convertToUserEntity(updateUser);

        // Giữ lại thông tin cũ cần thiết
        userEntity.setId(id); // BẮT BUỘC: để Hibernate biết là update
        userEntity.setUserName(oldUser.getUserName()); // Giữ nguyên vì UNIQUE
        userEntity.setStatus(oldUser.getStatus());  // Giữ nguyên trạng thái
        userEntity.setPassword(oldUser.getPassword()); // Giữ nguyên password

        // Gán role mới
        userEntity.setRoles(Stream.of(role).toList()); //Trường roles thường không được map đúng qua ModelMapper khi là @ManyToMany quan hệ phức tạp nên cần phải set lại thủ công

        return userConverter.convertToUserDto(userRepository.save(userEntity));
    }


    @Override
    @Transactional
    public void updatePassword(long id, PasswordDTO passwordDTO) throws MyExceptionConfig {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())
                && passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {

            String encodedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
            userRepository.updatePasswordById(id, encodedPassword);

        } else {
            throw new MyExceptionConfig(SystemConstant.CHANGE_PASSWORD_FAIL);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setStatus(0);
        userRepository.save(userEntity);

    }
}
