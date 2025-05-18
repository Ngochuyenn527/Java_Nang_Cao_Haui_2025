package com.example.demo.repository;


import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.custom.BuildingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>, BuildingRepositoryCustom {

    List<BuildingEntity> findByIsActive(Integer isActive);
}

