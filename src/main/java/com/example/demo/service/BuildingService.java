package com.example.demo.service;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import java.util.List;

public interface BuildingService {

    //tìm theo các tiêu chí trong BuildingSearchRequest được nhập vào để tìm kiếm
    List<BuildingSearchResponse> searchBuildings(BuildingSearchRequest buildingSearchRequest);

    BuildingDTO getBuildingById(Long id);

    BuildingDTO addBuilding(BuildingDTO buildingDTO);

    BuildingDTO updateBuilding(Long id, BuildingDTO buildingDTO);

    void deleteBuilding(Long id);

}

