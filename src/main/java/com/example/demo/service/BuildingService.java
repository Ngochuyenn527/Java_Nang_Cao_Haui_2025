package com.example.demo.service;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public interface BuildingService {

    List<BuildingSearchResponse> searchBuildings(BuildingSearchRequest buildingSearchRequest);

    BuildingDTO getBuildingById(Long id);

    BuildingDTO addBuilding(BuildingDTO buildingDTO);

    BuildingDTO updateBuilding(Long id, BuildingDTO buildingDTO);

    void deleteBuilding(Long id);
    
    long getDistinctProjectCount();
    
    long getDistinctAddress();
    
    double getDistinctAverage();
    
    double getavgPricePerM2();
    
    ObservableList<PieChart.Data> getPieChartDataForProjectsByDistrict();
        
    List<XYChart.Series<String, Number>> getBarChartDataForProjectsByParkingFees();
}

