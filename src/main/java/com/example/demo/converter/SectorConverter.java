package com.example.demo.converter;

import com.example.demo.entity.SectorEntity;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.repository.SectorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectorConverter {

    @Autowired
    private ModelMapper modelMapper;

    public SectorDTO convertToSectorDto(SectorEntity entity) {
        return modelMapper.map(entity, SectorDTO.class);
    }

    public SectorEntity convertToSectorEntity(SectorDTO sectorDTO) {
        return modelMapper.map(sectorDTO, SectorEntity.class);
    }
}
