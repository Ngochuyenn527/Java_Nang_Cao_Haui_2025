package com.example.demo.converter;

import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.RoleDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    // Chuyển đổi UserEntity (BE) thành UserDTO (FE)
    public UserDTO convertToUserDto(UserEntity entity) {
        UserDTO dto = modelMapper.map(entity, UserDTO.class);

        if (entity.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setCode(entity.getRole().getCode());
            dto.setRoleCode(roleDTO.getCode());
        }

        return dto;
    }

    // Chuyển đổi UserDTO (FE) thành UserEntity (BE)
    public UserEntity convertToUserEntity(UserDTO dto) {
        UserEntity entity = modelMapper.map(dto, UserEntity.class);

        if (dto.getRoleCode() != null) {
            RoleEntity role = roleRepository.findOneByCode(dto.getRoleCode());
            if (role == null) {
                throw new RuntimeException("Không tìm thấy role với code: " + dto.getRoleCode());
            }
            entity.setRole(role);
        }

        return entity;
    }
}
