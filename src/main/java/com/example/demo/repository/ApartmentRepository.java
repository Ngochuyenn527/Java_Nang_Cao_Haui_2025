package com.example.demo.repository;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.repository.custom.ApartmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long>, ApartmentRepositoryCustom {

    List<ApartmentEntity> findByIsActive(Integer isActive);

}
