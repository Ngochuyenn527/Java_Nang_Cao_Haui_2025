package com.example.demo.service.impl;

import com.example.demo.config.MyExceptionConfig;
import com.example.demo.constant.SystemConstant;
import com.example.demo.converter.UserConverter;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    // ✅ Kiểm tra user theo ID có tồn tại không, nếu không có thì ném ngoại lệ
    public UserEntity checkUserById(Long id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + id));
       return existingUser;
    }

    @Override
    public List<UserDTO> getAllUsersHasStatus1() {
        List<UserEntity> userEntities = userRepository.getAllUsersHasStatus1();
        List<UserDTO> results = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDTO userDTO = userConverter.convertToUserDto(userEntity);
            results.add(userDTO);
        }
        return results;
    }


    @Override
    public UserDTO getUserById(long id) {
        return userConverter.convertToUserDto(userRepository.findById(id).get());
    }


    @Override
    public UserDTO getUserByUserNameAndStatus(String name, int status) {
        return userConverter.convertToUserDto(userRepository.findOneByUserNameAndStatus(name, status));
    }


    @Override
    public List<UserDTO> getUsersByRoleCode(String roleCode) {
        List<UserEntity> entities = userRepository.getByRoleCode(roleCode);
        return entities.stream().map(userConverter::convertToUserDto).toList();
    }


    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        RoleEntity role = roleRepository.findOneByCode(userDTO.getRoleCode());
        if (role == null) {
            throw new RuntimeException("Không tìm thấy role với code: " + userDTO.getRoleCode());
        }

        // Map lại entity từ DTO
        UserEntity userEntity = userConverter.convertToUserEntity(userDTO);

        userEntity.setRole(role);
        userEntity.setStatus(1);
        userEntity.setPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));

        return userConverter.convertToUserDto(userRepository.save(userEntity));
    }


    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity existingUser = checkUserById(id);

        RoleEntity role = roleRepository.findOneByCode(userDTO.getRoleCode());
        if (role == null) {
            throw new RuntimeException("Không tìm thấy role với code: " + userDTO.getRoleCode());
        }

        // Map lại entity từ DTO
        UserEntity userEntity = userConverter.convertToUserEntity(userDTO);

        // Giữ lại thông tin cũ cần thiết
        userEntity.setId(id); // BẮT BUỘC: để Hibernate biết là update
        userEntity.setUserName(existingUser.getUserName()); // Giữ nguyên vì UNIQUE
        userEntity.setStatus(existingUser.getStatus());  // Giữ nguyên trạng thái
        userEntity.setPassword(existingUser.getPassword()); // Giữ nguyên password
        userEntity.setRole(role);

        return userConverter.convertToUserDto(userRepository.save(userEntity));
    }


    @Override
    @Transactional
    public void updatePassword(long id, PasswordDTO passwordDTO) throws MyExceptionConfig {
        UserEntity existingUser = checkUserById(id);

        if (passwordEncoder.matches(passwordDTO.getOldPassword(), existingUser.getPassword())
                && passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {

            String encodedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
            userRepository.updatePasswordById(id, encodedPassword);

        } else {
            throw new MyExceptionConfig(SystemConstant.CHANGE_PASSWORD_FAIL);
        }
    }

    @Override
    @Transactional
    public UserDTO resetPassword(long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));
        return userConverter.convertToUserDto(userRepository.save(userEntity));
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user với ID: " + id);
        }
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setStatus(0);
        userRepository.save(userEntity);
    }

}
