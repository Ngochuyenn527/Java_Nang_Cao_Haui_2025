package com.example.demo.service.impl;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.repository.ApartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ApartmentTrashServiceImpl extends TrashServiceImpl<ApartmentEntity> {
    public ApartmentTrashServiceImpl(ApartmentRepository repository) {
        super(repository);
    }
}

