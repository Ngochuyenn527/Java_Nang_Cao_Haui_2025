package com.example.demo.service.impl;

import com.example.demo.config.MyExceptionConfig;
import com.example.demo.config.UserPrincipal;
import com.example.demo.constant.SystemConstant;
import com.example.demo.converter.UserConverter;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.dto.UserRequestDTO;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private OtpService otpService;

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
    public UserDTO getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID must not be null!");
        }
        // Chuyển đổi Entity -> DTO
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
    public UserDTO addUser(UserDTO userDTO) {
        try {
            // Map lại entity từ DTO
            UserEntity userEntity = userConverter.convertToUserEntity(userDTO);

            userEntity.setStatus(1);
            userEntity.setPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));

            userRepository.save(userEntity);
            return userConverter.convertToUserDto(userEntity);
        } catch (Exception e) {
        	throw new RuntimeException("Có lỗi xảy ra khi cập nhật user: " + e.getMessage());
        }
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            UserEntity existingUser = checkUserById(id);
            UserEntity updatedUser = userConverter.convertToUserEntity(userDTO);

            updatedUser.setId(id);
            updatedUser.setUserName(existingUser.getUserName());
            updatedUser.setStatus(existingUser.getStatus());
            updatedUser.setPassword(existingUser.getPassword());

            userRepository.save(updatedUser);
            return userConverter.convertToUserDto(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật user: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void updatePassword(Long id, PasswordDTO passwordDTO) throws MyExceptionConfig {
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
    public UserDTO resetPassword(Long id) {
        try {
            UserEntity entity = checkUserById(id);
            entity.setPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));
            userRepository.save(entity);
            return userConverter.convertToUserDto(entity);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi reset mật khẩu: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            UserEntity entity = checkUserById(id);
            entity.setStatus(0);
            userRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa user: " + e.getMessage());
        }
    }
    
    @Override
    public void register(UserRequestDTO dto) {
        if (!otpService.validateOtp(dto.getEmail(), dto.getOtp())) {
            throw new RuntimeException("Mã OTP không đúng hoặc đã hết hạn");
        }

        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        UserEntity user = UserEntity.builder()
                .userName(dto.getUserName())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .status(1)
                .role(roleRepository.findOneByCode("STAFF"))
                .build();

        userRepository.save(user);
    }
    
    @Override
    public UserDTO login(String userName, String password) {
        UserEntity user = userRepository.findOneByUserNameAndStatus(userName, 1); // status=1 active
        if (user == null) {
            throw new RuntimeException("Tên đăng nhập không tồn tại hoặc bị khóa");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
        return userConverter.convertToUserDto(user);
    }
    
    @Override
    public UserDTO getCurrentUser(UserPrincipal principal) {
        UserEntity user = userRepository.getUser(principal);
        return userConverter.convertToUserDto(user);
    }
}
