package com.example.demo.repository.custom;

import com.example.demo.entity.ApartmentEntity;

import java.util.List;

public interface ApartmentRepositoryCustom {

    List<ApartmentEntity> getByStatus(String status);

    List<ApartmentEntity> getByFloor(Integer floor);
}

