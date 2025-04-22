package com.example.demo.converter;

import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.BuildingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//chuyển đổi kiểu dữ liệu của building giữa fe và be
@Component
public class BuildingConverter {

    @Autowired
    private ModelMapper modelMapper;

    //chuyển đổi BuildingEntity (be) thành BuildingDTO (fe) dùng modelMapper
    public BuildingDTO convertToBuildingDTO(BuildingEntity buildingEntity) {
      return modelMapper.map(buildingEntity, BuildingDTO.class);
    }

    //chuyển đổi BuildingDTO (fe) thành BuildingEntity (be) dùng modelMapper
    public BuildingEntity convertToBuildingEntity(BuildingDTO buildingDTO) {
        return modelMapper.map(buildingDTO, BuildingEntity.class);
    }
}
