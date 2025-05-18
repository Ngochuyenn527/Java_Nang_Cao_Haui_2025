package com.example.demo.service.impl;

import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.BuildingRepository;
import org.springframework.stereotype.Service;

@Service
public class BuildingTrashServiceImpl extends TrashServiceImpl<BuildingEntity> {
    public BuildingTrashServiceImpl(BuildingRepository repository) {
        super(repository);
    }
}
