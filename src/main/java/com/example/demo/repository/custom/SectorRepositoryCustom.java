package com.example.demo.repository.custom;

import com.example.demo.entity.SectorEntity;

import java.util.List;

public interface SectorRepositoryCustom {
    List<SectorEntity> getByLocation(String location);

    List<SectorEntity> getByStatus(String status);
}
