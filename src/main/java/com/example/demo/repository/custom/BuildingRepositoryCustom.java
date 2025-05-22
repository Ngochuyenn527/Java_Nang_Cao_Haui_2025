package com.example.demo.repository.custom;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.entity.BuildingEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

// class tùy chỉnh (custom) cho repository, dùng để viết các câu SQL nâng cao (native SQL hoặc JPQL) mà Spring Data JPA mặc định không hỗ trợ.
public interface BuildingRepositoryCustom {
    List<BuildingEntity> searchBuildings(BuildingSearchBuilder buildingSearchBuilder);
    long countDistinctProjectName();
    
    long countDistinctAdress();
    
    double countDistinctAverage();
    
    Map<String, Integer> getProjectCountByDistrict();
}
