package com.example.demo.controller;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @Operation(summary = "API get buildings has isActive = 1")
    @GetMapping
    public ResponseEntity<List<BuildingDTO>> getActiveBuildings() {
        return ResponseEntity.ok(buildingService.getBuildingsByActive(1));
    }

    @Operation(summary = "API search building")
    @GetMapping("/search")
    public List<BuildingSearchResponse> searchBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest) {
        List<BuildingSearchResponse> res = buildingService.searchBuildings(buildingSearchRequest);
        return res;
    }

    @Operation(summary = "API get building by id")
    @GetMapping("/{id}")
    public ResponseEntity<BuildingDTO> getBuildingById(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @Operation(summary = "API add new building")
    @PostMapping
    public ResponseEntity<BuildingDTO> addBuilding(@RequestBody BuildingDTO buildingDTO) {
        return ResponseEntity.ok(buildingService.addBuilding(buildingDTO));
    }

    @Operation(summary = "API update building by id")
    @PutMapping("/{id}")
    public ResponseEntity<BuildingDTO> updateBuilding(@PathVariable Long id, @RequestBody BuildingDTO buildingDTO) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, buildingDTO));
    }

    @Operation(summary = "API get buildings in trash has status = 0")
    @GetMapping("/trash")
    public ResponseEntity<List<BuildingDTO>> getBuildingsInTrash() {
        return ResponseEntity.ok(buildingService.getBuildingsByActive(0));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "API move buildings in trash")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        buildingService.moveToTrash(id);
        return ResponseEntity.ok("Moved building successfully!");
    }

    @Operation(summary = "API restore buildings in trash")
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        buildingService.restoreFromTrash(id);
        return ResponseEntity.ok("Restored building successfully!");
    }

    @Operation(summary = "API permanently delete building")
    @DeleteMapping("/permanent-delete/{id}")
    public ResponseEntity<String> permanentDelete(@PathVariable Long id) {
        buildingService.deletePermanently(id);
        return ResponseEntity.ok("Permanently deleted building successfully!");
    }

    @Operation(summary = "API delete all records in trash")
    @DeleteMapping("/trash")
    public ResponseEntity<?> deleteAllTrash() {
        buildingService.deleteAllInTrash();
        return ResponseEntity.ok("Delete all records in trash successfully!");
    }

    @Operation(summary = "API delete all records older than 30 days")
    @DeleteMapping("/trash/expired")
    public ResponseEntity<?> deleteExpiredTrash() {
        buildingService.deleteExpiredTrash();
        return ResponseEntity.ok("Delete all records older than 30 days successfully!");
    }
}