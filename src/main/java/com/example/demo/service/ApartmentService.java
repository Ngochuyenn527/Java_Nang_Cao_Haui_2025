package com.example.demo.service;


import com.example.demo.entity.ApartmentEntity;
import com.example.demo.model.dto.ApartmentDTO;

import java.util.List;

public interface ApartmentService extends TrashService<ApartmentEntity> {

    List<ApartmentDTO> getApartmentsByActive(int isActive);

    ApartmentDTO getApartmentById(Long id);

    List<ApartmentDTO> getApartmentsByFloor(Integer floor);

    List<ApartmentDTO> getApartmentsByStatus(String status);

    ApartmentDTO addApartment(ApartmentDTO apartmentDTO);

    ApartmentDTO updateApartment(Long id, ApartmentDTO apartmentDTO);

    void moveToTrash(Long id);
}

