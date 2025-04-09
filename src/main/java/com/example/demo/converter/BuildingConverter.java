package com.example.demo.converter;

import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.BuildingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuildingConverter {

    @Autowired
    private ModelMapper modelMapper;

    //chuyển đổi BuildingEntity (be) thành BuildingDTO (fe) dùng modelMapper
    public BuildingDTO toBuildingDTO(BuildingEntity buildingEntity) {
      return modelMapper.map(buildingEntity, BuildingDTO.class);
    }

    //chuyển đổi BuildingDTO (fe) thành BuildingEntity (be) dùng modelMapper
    public BuildingEntity toBuildingEntity(BuildingDTO buildingDTO) {
        return modelMapper.map(buildingDTO, BuildingEntity.class);
    }
}
