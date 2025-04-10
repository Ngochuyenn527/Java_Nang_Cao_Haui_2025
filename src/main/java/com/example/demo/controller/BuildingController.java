package com.example.demo.controller;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/building")
@Validated
public class BuildingController {

    @Autowired
    private BuildingService buildingService;


    @Operation(summary = "API get building")
    @GetMapping
    public List<BuildingSearchResponse> searchBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest) {
        List<BuildingSearchResponse> res = buildingService.searchBuildings(buildingSearchRequest);
        return res;
    }

    @Operation(summary = "API add new building")
    @PostMapping
    public ResponseEntity<BuildingDTO> addBuilding(@Valid @RequestBody BuildingDTO buildingDTO) {
        BuildingDTO savedBuilding = buildingService.addBuilding(buildingDTO);
        return ResponseEntity.ok(savedBuilding);
    }

    @Operation(summary = "API update building by id")
    @PutMapping("/{id}")
    public ResponseEntity<BuildingDTO> updateBuilding(@Valid @PathVariable Long id, @RequestBody BuildingDTO buildingDTO) {
        BuildingDTO updatedBuilding = buildingService.updateBuilding(id, buildingDTO);
        return ResponseEntity.ok(updatedBuilding);
    }

    @Operation(summary = "API delete building by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.ok("Deleted successfully!");
    }


}