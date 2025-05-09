package com.example.demo.service.impl;

import com.example.demo.converter.SectorConverter;
import com.example.demo.entity.SectorEntity;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.repository.SectorRepository;
import com.example.demo.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SectorConverter sectorConverter;

    // Kiểm tra xem phân khu có tồn tại hay không
    private SectorEntity checkSectorById(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân khu với ID: " + id));
    }

    @Override
    public List<SectorDTO> getAllSectors() {
        return sectorRepository.findAll().stream()
                .map(sectorConverter::convertToSectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public SectorDTO getSectorById(long id) {
        return sectorConverter.convertToSectorDto(checkSectorById(id));
    }

    @Override
    public List<SectorDTO> getSectorsByLocation(String location) {
        List<SectorEntity> sectorEntities = sectorRepository.getByLocation(location);
        return sectorEntities.stream()
                .map(sectorConverter::convertToSectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SectorDTO> getSectorsByStatus(String status) {
        List<SectorEntity> sectorEntities = sectorRepository.getByStatus(status);
        return sectorEntities.stream()
                .map(sectorConverter::convertToSectorDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectorDTO addSector(SectorDTO sectorDTO) {
        try {
            SectorEntity sectorEntity = sectorConverter.convertToSectorEntity(sectorDTO);
            sectorRepository.save(sectorEntity);
            return sectorDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm phân khu: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SectorDTO updateSector(Long id, SectorDTO sectorDTO) {
        try {
            SectorEntity existingSector = checkSectorById(id);

            SectorEntity updatedSector = sectorConverter.convertToSectorEntity(sectorDTO);
            updatedSector.setId(id);

            sectorRepository.save(updatedSector);
            return sectorDTO;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật phân khu: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteSector(Long id) {
        try {
            SectorEntity existingSector = checkSectorById(id);
            sectorRepository.delete(existingSector);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa phân khu: " + e.getMessage());
        }
    }
}
