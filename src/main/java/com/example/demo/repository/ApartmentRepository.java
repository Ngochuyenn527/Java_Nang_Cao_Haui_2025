package com.example.demo.repository;

import com.example.demo.entity.ApartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long> {

    List<ApartmentEntity> findByStatus(String status);

    List<ApartmentEntity> findByFloor(Integer floor);

    List<ApartmentEntity> findByIsActive(Integer isActive);

}
