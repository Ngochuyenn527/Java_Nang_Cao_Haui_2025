package com.example.demo.repository.custom;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.entity.BuildingEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

//Custom dùng để chứa các phương thức không được thư viện JpaRepository hỗ trợ
public interface BuildingRepositoryCustom {
    List<BuildingEntity> searchBuildings(BuildingSearchBuilder buildingSearchBuilder);
    
    long countDistinctProjectName();
    
    long countDistinctAdress();
    
    double countDistinctAverage();
    
    double avgPricePerM2();
    
    Map<String, Integer> getProjectCountByDistrict();
        
    Map<String, Integer> getProjectCountByBikeParkingRange();
    Map<String, Integer> getProjectCountByCarParkingRange();
}
