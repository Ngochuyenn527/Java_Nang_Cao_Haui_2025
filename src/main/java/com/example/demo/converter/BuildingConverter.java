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

    @Autowired
    private SectorConverter sectorConverter;

    //chuyển đổi BuildingEntity (be) thành BuildingDTO (fe) dùng modelMapper
    public BuildingDTO convertToBuildingDTO(BuildingEntity entity) {
        BuildingDTO dto = modelMapper.map(entity, BuildingDTO.class);

        //gán giá trị cho SectorDTO sector khi sector_id được thêm trong Building
        if (dto.getSector() != null && dto.getSector().getId() != null) {
            dto.setSector(sectorConverter.convertToSectorDto(entity.getSector()));
        }
        return dto;
    }

    //chuyển đổi BuildingDTO (fe) thành BuildingEntity (be) dùng modelMapper
    public BuildingEntity convertToBuildingEntity(BuildingDTO dto) {
        BuildingEntity entity = modelMapper.map(dto, BuildingEntity.class);

        //gán giá trị cho SectorDTO sector khi sector_id được thêm trong Building
        if (dto.getSector() != null && dto.getSector().getId() != null) {
            entity.setSector(sectorConverter.convertToSectorEntity(dto.getSector()));
        }
        return entity;
    }
}
