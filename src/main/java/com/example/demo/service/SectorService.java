package com.example.demo.service;

import com.example.demo.entity.SectorEntity;
import com.example.demo.model.dto.SectorDTO;

import java.util.List;

public interface SectorService extends TrashService<SectorEntity> {

    List<SectorDTO> getSectorsByActive(int isActive);

    SectorDTO getSectorById(long id);

    List<SectorDTO> getSectorsByLocation(String location);

    List<SectorDTO> getSectorsByStatus(String status);

    SectorDTO addSector(SectorDTO sectorDTO);

    SectorDTO updateSector(Long id, SectorDTO sectorDTO);

    void moveToTrash(Long id);

}
