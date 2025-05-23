package com.example.demo.service;

import com.example.demo.model.dto.SectorDTO;

import java.util.List;
import java.util.Map;

public interface SectorService {

    // Lấy tất cả các phân khu
    List<SectorDTO> getAllSectors();

    // Tìm phân khu theo ID
    SectorDTO getSectorById(long id);

    // Tìm phân khu theo location
    List<SectorDTO> getSectorsByLocation(String location);

    // Tìm phân khu theo status
    List<SectorDTO> getSectorsByStatus(String status);

    // Thêm phân khu mới
    SectorDTO addSector(SectorDTO sectorDTO);

    // Cập nhật thông tin phân khu
    SectorDTO updateSector(Long id, SectorDTO sectorDTO);

    // Xóa phân khu
    void deleteSector(Long id);
    
    long getSectorCount();
    
    Map<Integer, Long> getSectorCountByYear();
}
