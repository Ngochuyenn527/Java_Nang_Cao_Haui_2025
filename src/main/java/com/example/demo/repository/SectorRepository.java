package com.example.demo.repository;

import com.example.demo.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long> {

    List<SectorEntity> findByLocation(String location);

    List<SectorEntity> findByStatus(String status);

    List<SectorEntity> findByIsActive(Integer isActive);

}

