package com.example.demo.service.impl;

import com.example.demo.entity.SectorEntity;
import com.example.demo.repository.SectorRepository;
import org.springframework.stereotype.Service;

@Service
public class SectorTrashServiceImpl extends TrashServiceImpl<SectorEntity> {
    public SectorTrashServiceImpl(SectorRepository repository) {
        super(repository);
    }
}

