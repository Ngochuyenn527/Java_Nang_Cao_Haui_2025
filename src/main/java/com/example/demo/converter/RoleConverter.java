package com.example.demo.converter;

import com.example.demo.entity.RoleEntity;
import com.example.demo.model.dto.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public RoleDTO convertToRoleDto(RoleEntity entity) {
        return modelMapper.map(entity, RoleDTO.class);
    }

    public RoleEntity convertToRoleEntity(RoleDTO dto) {
        return modelMapper.map(dto, RoleEntity.class);
    }
}
