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

    @Autowired
    private BuildingConverter buildingConverter;

    public ApartmentDTO convertToApartmentDto(ApartmentEntity entity) {
        ApartmentDTO dto = modelMapper.map(entity, ApartmentDTO.class);

        //gán giá trị cho BuildingDTO building khi building_id đưuọc thêm trong Apartment
        if (dto.getBuilding() != null && dto.getBuilding().getId() != null) {
            dto.setBuilding(buildingConverter.convertToBuildingDTO(entity.getBuilding()));
        }
        return dto;
    }

    public ApartmentEntity convertToApartmentEntity(ApartmentDTO dto) {
        ApartmentEntity entity = modelMapper.map(dto, ApartmentEntity.class);

        //gán giá trị cho BuildingDTO building khi building_id đưuọc thêm trong Apartment
        if (dto.getBuilding() != null && dto.getBuilding().getId() != null) {
            entity.setBuilding(buildingConverter.convertToBuildingEntity(dto.getBuilding()));
        }
        return entity;
    }
}

