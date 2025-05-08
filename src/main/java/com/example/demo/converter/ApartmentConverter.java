package com.example.demo.converter;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.model.dto.ApartmentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ApartmentDTO convertToApartmentDto(ApartmentEntity entity) {
        return modelMapper.map(entity, ApartmentDTO.class);
    }

    public ApartmentEntity convertToApartmentEntity(ApartmentDTO dto) {
        return modelMapper.map(dto, ApartmentEntity.class);
    }
}

