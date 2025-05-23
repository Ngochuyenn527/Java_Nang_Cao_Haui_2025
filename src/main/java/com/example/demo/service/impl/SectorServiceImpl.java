package com.example.demo.service.impl;

import com.example.demo.converter.SectorConverter;
import com.example.demo.entity.SectorEntity;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.repository.SectorRepository;
import com.example.demo.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SectorConverter sectorConverter;

    @Autowired
    private SectorTrashServiceImpl trashService;

    // Kiểm tra xem phân khu có tồn tại hay không
    private SectorEntity checkSectorById(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân khu với ID: " + id));
    }

    @Override
    public List<SectorDTO> getSectorsByActive(int isActive) {
        return sectorRepository.findByIsActive(isActive)
                .stream().map(sectorConverter::convertToSectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public SectorDTO getSectorById(long id) {
        return sectorConverter.convertToSectorDto(checkSectorById(id));
    }

    @Override
    public List<SectorDTO> getSectorsByLocation(String location) {
        return sectorRepository.findByLocation(location)
                .stream().map(sectorConverter::convertToSectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SectorDTO> getSectorsByStatus(String status) {
        return sectorRepository.findByStatus(status)
                .stream().map(sectorConverter::convertToSectorDto)
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
    public void moveToTrash(Long id) {
        try {
            SectorEntity entity = checkSectorById(id);
            entity.setIsActive(0);
            entity.setDeletedAt(LocalDateTime.now());
            sectorRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển vào thùng rác: " + e.getMessage(), e);
        }
    }

    @Override
    public void restoreFromTrash(Long id) {
        trashService.restoreFromTrash(id);
    }

    @Override
    public void deletePermanently(Long id) {
        trashService.deletePermanently(id);

    }

    @Override
    public void deleteAllInTrash() {
        trashService.deleteAllInTrash();
    }

    @Override
    public void deleteExpiredTrash() {
        trashService.deleteExpiredTrash();
    }
}
