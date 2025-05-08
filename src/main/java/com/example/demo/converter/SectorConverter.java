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

    @Autowired
    private SectorRepository sectorRepository;

    public SectorDTO convertToSectorDto(SectorEntity entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, SectorDTO.class);
    }

    public SectorEntity convertToSectorEntity(SectorDTO sectorDTO) {
        if (sectorDTO == null) return null;

        // Kiểm tra xem SectorEntity với id đã tồn tại trong DB chưa
        if (sectorDTO.getId() != null) {
            return sectorRepository.findById(sectorDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sector với id: " + sectorDTO.getId()));
        }

        // Nếu không có id hoặc không tìm thấy, tạo mới (tùy thuộc vào yêu cầu)
        SectorEntity sectorEntity = modelMapper.map(sectorDTO, SectorEntity.class);
        return sectorEntity;
    }
}
