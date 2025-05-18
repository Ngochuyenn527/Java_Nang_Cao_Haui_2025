package com.example.demo.controller;

import com.example.demo.model.dto.SectorDTO;
import com.example.demo.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/sector")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @Operation(summary = "API get sectors has isActive = 1")
    @GetMapping
    public ResponseEntity<List<SectorDTO>> getActiveSectors() {
        return ResponseEntity.ok(sectorService.getSectorsByActive(1));
    }


    @Operation(summary = "API get sector by id")
    @GetMapping("/{id}")
    public ResponseEntity<SectorDTO> getSectorById(@PathVariable Long id) {
        SectorDTO sectorDTO = sectorService.getSectorById(id);
        return new ResponseEntity<>(sectorDTO, HttpStatus.OK);
    }


    @Operation(summary = "API get sectors by location")
    @GetMapping("/location/{location}")
    public ResponseEntity<List<SectorDTO>> getSectorsByLocation(@PathVariable String location) {
        List<SectorDTO> sectors = sectorService.getSectorsByLocation(location);
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }


    @Operation(summary = "API get sectors by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SectorDTO>> getSectorsByStatus(@PathVariable String status) {
        List<SectorDTO> sectors = sectorService.getSectorsByStatus(status);
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }


    @Operation(summary = "API create new sector")
    @PostMapping
    public ResponseEntity<SectorDTO> addSector(@RequestBody SectorDTO sectorDTO) {
        SectorDTO createdSector = sectorService.addSector(sectorDTO);
        return new ResponseEntity<>(createdSector, HttpStatus.CREATED);
    }


    @Operation(summary = "API update sector by id")
    @PutMapping("/{id}")
    public ResponseEntity<SectorDTO> updateSector(@PathVariable Long id, @RequestBody SectorDTO sectorDTO) {
        SectorDTO updatedSector = sectorService.updateSector(id, sectorDTO);
        return new ResponseEntity<>(updatedSector, HttpStatus.OK);
    }

    @Operation(summary = "API get sectors in trash has isActive = 0")
    @GetMapping("/trash")
    public ResponseEntity<List<SectorDTO>> getSectorsInTrash() {
        return ResponseEntity.ok(sectorService.getSectorsByActive(0));
    }

    @Operation(summary = "API move sector to trash")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteSector(@PathVariable Long id) {
        sectorService.moveToTrash(id);
        return ResponseEntity.ok("Moved sector to trash successfully!");
    }

    @Operation(summary = "API restore sector from trash")
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreSector(@PathVariable Long id) {
        sectorService.restoreFromTrash(id);
        return ResponseEntity.ok("Restored sector successfully!");
    }

    @Operation(summary = "API permanently delete sector")
    @DeleteMapping("/permanent-delete/{id}")
    public ResponseEntity<String> permanentDeleteSector(@PathVariable Long id) {
        sectorService.deletePermanently(id);
        return ResponseEntity.ok("Permanently deleted sector successfully!");
    }

    @Operation(summary = "API delete all sectors in trash")
    @DeleteMapping("/trash")
    public ResponseEntity<String> deleteAllSectorsInTrash() {
        sectorService.deleteAllInTrash();
        return ResponseEntity.ok("Deleted all sectors in trash successfully!");
    }

    @Operation(summary = "API delete expired sectors in trash")
    @DeleteMapping("/trash/expired")
    public ResponseEntity<String> deleteExpiredSectors() {
        sectorService.deleteExpiredTrash();
        return ResponseEntity.ok("Deleted expired sectors from trash successfully!");
    }
}

