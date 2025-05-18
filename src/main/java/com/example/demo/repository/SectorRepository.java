package com.example.demo.repository;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.entity.SectorEntity;
import com.example.demo.repository.custom.SectorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long>, SectorRepositoryCustom {

    List<SectorEntity> findByIsActive(Integer isActive);

}

