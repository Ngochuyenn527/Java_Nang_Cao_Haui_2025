package com.example.demo.converter;


import com.example.demo.entity.UserEntity;
import com.example.demo.model.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO convertToUserDto (UserEntity entity){
        return modelMapper.map(entity, UserDTO.class);
    }

    public UserEntity convertToUserEntity (UserDTO dto){
        return  modelMapper.map(dto, UserEntity.class);
    }
    
}
