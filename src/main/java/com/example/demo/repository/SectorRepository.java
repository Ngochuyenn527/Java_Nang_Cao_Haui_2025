package com.example.demo.repository;

import com.example.demo.entity.SectorEntity;
import com.example.demo.repository.custom.SectorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long>, SectorRepositoryCustom {
}

