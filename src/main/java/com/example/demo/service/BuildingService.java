package com.example.demo.service;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import java.util.List;

public interface BuildingService {

    List<BuildingDTO> getAllBuildings();

    List<BuildingSearchResponse> searchBuildings(BuildingSearchRequest buildingSearchRequest);

    BuildingDTO getBuildingById(Long id);

    BuildingDTO addBuilding(BuildingDTO buildingDTO);

    BuildingDTO updateBuilding(Long id, BuildingDTO buildingDTO);

    void deleteBuilding(Long id);

}

