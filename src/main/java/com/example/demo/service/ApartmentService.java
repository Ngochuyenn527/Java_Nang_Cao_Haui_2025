package com.example.demo.service;


import com.example.demo.entity.ApartmentEntity;
import com.example.demo.model.dto.ApartmentDTO;

import java.util.List;

public interface ApartmentService {

    List<ApartmentDTO> getAllApartments();

    ApartmentDTO getApartmentById(Long id);

    List<ApartmentDTO> getApartmentsByFloor(Integer floor);

    List<ApartmentDTO> getApartmentsByStatus(String status);

    ApartmentDTO addApartment(ApartmentDTO apartmentDTO);

    ApartmentDTO updateApartment(Long id, ApartmentDTO apartmentDTO);

    void deleteApartment(Long id);
}

