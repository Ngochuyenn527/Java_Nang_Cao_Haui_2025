package com.example.demo.repository;


import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.custom.BuildingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>, BuildingRepositoryCustom {
    //Kế thừa JpaRepository<BuildingEntity, Long> để có sẵn các phương thức save, findById, delete,...
    //Không cần viết SQL thủ công vì Spring Data JPA hỗ trợ tự động.

}

